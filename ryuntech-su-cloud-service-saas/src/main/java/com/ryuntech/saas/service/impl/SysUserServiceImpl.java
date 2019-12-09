package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.ryuntech.common.constant.RedisConstant;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.constant.enums.RolePermEnum;
import com.ryuntech.common.constant.enums.SysRoleEnum;
import com.ryuntech.common.constant.generator.IncrementIdGenerator;
import com.ryuntech.common.constant.generator.UniqueIdGenerator;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.HttpUtils;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.common.utils.redis.JedisUtil;
import com.ryuntech.saas.api.dto.LoginConpanyDTO;
import com.ryuntech.saas.api.dto.LoginDTO;
import com.ryuntech.saas.api.dto.Sms;
import com.ryuntech.saas.api.dto.SysUserDTO;
import com.ryuntech.saas.api.helper.constant.ApiConstants;
import com.ryuntech.saas.api.helper.constant.SmsConstants;
import com.ryuntech.saas.api.mapper.*;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.MessageSendService;
import com.ryuntech.saas.api.service.SysUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.ryuntech.saas.api.helper.constant.ApiConstants.APPKEY;

/**
 * @author antu
 * @date 2019-05-22
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRolePermMapper sysRolePermMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private JedisUtil jedisUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public SysUser findByName(String username) {
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        return sysUser;
    }

    @Override
    public List<SysUser> list(SysUser user) {
        QueryWrapper queryWrapper = null;
        /*if (user.getUsername() != null && !user.getUsername().isEmpty()) {
             queryWrapper= new QueryWrapper<SysUser>().eq("username", user.getUsername());
        }*/
        //插入权限数据
        return sysUserMapper.selectList(queryWrapper);
    }

    @Override
    public void create(SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
    }

    @Override
    public void update(SysUser user) {
        user.setPassword(null);
        sysUserMapper.updateById(user);
    }

    @Override
    public SysUser selectUserRoleById(SysUser user) {
        return sysUserMapper.selectUserRoleById(user);
    }

    @Override
    public IPage<SysUser> selectUsersRoleById(SysUser user, QueryPage queryPage) {

        Page<SysUser> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (user.getSysUserId() != null) {
            queryWrapper.eq("id", user.getSysUserId());
        }
        /*if (user.getUsername()!=null) {
            queryWrapper.eq("username", user.getUsername());
        }*/
        return sysUserMapper.selectUsersRoleById(page, user);
    }

    @Override
    public List<SysUser> selectUserMap(SysUser user) {
        return sysUserMapper.selectUserMap(user);
    }

    @Override
    public SysUser selectByUserDTO(SysUserDTO userDTO) {
        if (userDTO.getUsername() != null) {
            queryWrapper.eq("username", userDTO.getUsername());
        }
        if (userDTO.getMobile() != null) {
            queryWrapper.eq("mobile", userDTO.getMobile());
        }
        if (StringUtils.isNotBlank(userDTO.getSysUserId())) {
            queryWrapper.eq("sys_user_id", userDTO.getSysUserId());
        }
        if (userDTO.getUnionId() != null) {
            queryWrapper.eq("union_id", userDTO.getUnionId());
        }
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public SysUser selectByUser(SysUser user) {
        if (StringUtils.isNotBlank(user.getSysUserId())) {
            queryWrapper.eq("id", user.getSysUserId());
        }
        if (user.getMobile() != null) {
            queryWrapper.eq("mobile", user.getMobile());
        }
        return sysUserMapper.selectOne(queryWrapper);
    }


    /**
     * 获取Auth Code
     *
     * @return
     */
    protected static final String[] randomAuthentHeader() {
        String timeSpan = String.valueOf(System.currentTimeMillis() / 1000);
        return new String[]{DigestUtils.md5Hex(APPKEY.concat(timeSpan).concat(ApiConstants.SECKEY)).toUpperCase(), timeSpan};
    }


    @Override
    public boolean sendRegisterSms(String mobile) throws Exception {
        // 手机号码已经存在sysuser表，则不允许继续注册，后面走添加公司流程
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("mobile", mobile));
        if (sysUser != null && "1".equals(sysUser.getStatus())) {
            throw new Exception("用户账号已经存在，不能重复注册");
        }

        // 手机号码发送的频率限制
        if (jedisUtil.exists(RedisConstant.PRE_SMS_EXIST_IN_MINUTE + SmsConstants.TEMPLATECODE + ":" + mobile)) {
            throw new Exception("您获取验证码太频繁，每分钟仅支持发送一条短信");
        }

        return messageSendService.send(new Sms(mobile, SmsConstants.TEMPLATECODE, 1, StringUtil.sixRandomNum()));
    }

    @Override
    public Result login(String username, String password) {
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        if (sysUser == null || !new BCryptPasswordEncoder().matches(password, sysUser.getPassword())) {
            return new Result(CommonEnums.LOGIN_ERROR.getMsg());
        }

        List<LoginConpanyDTO> list = companyMapper.listByUsername(username);
        if (list == null || list.size() == 0) {
            return new Result(CommonEnums.OPERATE_ERROR, "未找到公司信息");
        }

        List<String> ids = list.stream().map(l -> l.getCompanyId()).collect(Collectors.toList());

        String sysUserId = sysUser.getSysUserId();
        jedisUtil.sadd(RedisConstant.PRE_LOGIN_USER_SUCCESS + sysUserId, ids.toArray(new String[ids.size()]));
        jedisUtil.expire(RedisConstant.PRE_LOGIN_USER_SUCCESS + sysUserId, RedisConstant.PRE_LOGIN_USER_SUCCESS_EXPIRE);
        return new Result(new LoginDTO(list, sysUserId));
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean saveRegister(String companyName, String employeeName, String mobile, String password) throws Exception {
        // 校验公司是否存在我们数据库
        Company company = companyMapper.selectOne(new QueryWrapper<Company>().eq("name", companyName));
        if (company != null && !company.getIsDel()) {
            throw new Exception(companyName + "已经存在");
        }

        //TODO 校验公司是否真实存在（企查查接口）
        UniqueIdGenerator uniqueIdGenerator = UniqueIdGenerator.getInstance(IncrementIdGenerator.getServiceId());
        Date time = new Date();
        String adminRoleId = null;
        // 添加公司表数据
        company = new Company();
        company.setCompanyId(uniqueIdGenerator.nextStrId());
        company.setName(companyName);
        company.setIsDel(false);
        company.setCreatedAt(time);
        company.setUpdatedAt(time);
        String[] autherHeader = randomAuthentHeader();
        HashMap<String, String> reqHeader = new HashMap<>();
        reqHeader.put("Token", autherHeader[0]);
        reqHeader.put("Timespan", autherHeader[1]);
        Gson gson = new Gson();
        String urlName = ApiConstants.GETBASICDETAILSBYNAME + "?key=" + APPKEY + "&keyWord=" + companyName;
        String content = HttpUtils.Get(urlName, reqHeader);
        ApiGetBasicDetailsByName apiGetBasicDetailsByName = gson.fromJson(content, ApiGetBasicDetailsByName.class);
        if (null == apiGetBasicDetailsByName) {
            company.setIsQichacha(false);
        }
        if (apiGetBasicDetailsByName != null && StringUtils.isNotBlank(apiGetBasicDetailsByName.getStatus())) {
            String status = apiGetBasicDetailsByName.getStatus();
            if (!status.equals("200")){
                company.setIsQichacha(false);
            }else {
                company.setIsQichacha(true);
//                查询法人
                String operName = apiGetBasicDetailsByName.getResult().getOperName();
                company.setOperName(operName);
            }
        }

        companyMapper.insert(company);

        // 初始化角色信息
        List<SysRole> roleList = initRole();
        for (SysRole sysRole : roleList) {
            sysRole.setRid(uniqueIdGenerator.nextStrId());
            if (sysRole.getIsAdmin() == 1) {
                adminRoleId = sysRole.getRid();
            }
            sysRole.setCompanyId(company.getCompanyId());
            sysRole.setCreated(time);
            sysRole.setUpdated(time);
            sysRoleMapper.insert(sysRole);

            List<SysRolePerm> list = null;
            switch (SysRoleEnum.getByValue(sysRole.getRname())) {
                case ADMIN:
                    list = initAdminRolePerm(sysRole.getRid());
                    break;
                case BUSINESS:
                    list = initBusinessRolePerm(sysRole.getRid());
                    break;
                case LEADER:
                    list = initLeaderRolePerm(sysRole.getRid());
                    break;
                case FINANCE:
                    list = initFinanceRolePerm(sysRole.getRid());
                    break;
                case CREDIT:
                    list = initCreditRolePerm(sysRole.getRid());
                    break;
            }

            // 初始化角色-资源关联表
            for (SysRolePerm sysRolePerm : list) {
                sysRolePermMapper.insert(sysRolePerm);
            }
        }

        // 初始化部门信息
        Department department = new Department();
        department.setDepartmentId(uniqueIdGenerator.nextStrId());
        department.setCompanyId(company.getCompanyId());
        department.setLevel("1");
        department.setCreatedAt(time);
        department.setUpdatedAt(time);
        departmentMapper.insert(department);

        // 添加用户表数据
        SysUser sysUser = new SysUser();
        sysUser.setSysUserId(uniqueIdGenerator.nextStrId());
        sysUser.setUsername(mobile);
        sysUser.setMobile(mobile);
        sysUser.setPassword(new BCryptPasswordEncoder().encode(password));
        sysUser.setStatus("1");
        sysUser.setCreatedAt(time);
        sysUser.setUpdatedAt(time);
        sysUserMapper.insert(sysUser);

        // 添加员工表数据
        Employee employee = new Employee();
        employee.setEmployeeId(uniqueIdGenerator.nextStrId());
        employee.setSysUserId(sysUser.getSysUserId());
        employee.setName(employeeName);
        employee.setCompanyId(company.getCompanyId());
        employee.setCompanyName(company.getName());
        employee.setDepartmentId(department.getDepartmentId());
        employee.setIsCharger("1");
        employee.setDataType(3);
        employee.setStatus(1);
        employee.setCreatedAt(time);
        employee.setUpdatedAt(time);
        employeeMapper.insert(employee);

        // 给员工添加默认管理员角色信息
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setEmployeeId(employee.getEmployeeId());
        sysUserRole.setRoleId(adminRoleId);
        sysUserRoleMapper.insert(sysUserRole);
        return true;
    }


    private List<SysRolePerm> initAdminRolePerm(String rid) {
        List<SysRolePerm> rolePermList = new ArrayList<>();
        List<String> list = RolePermEnum.list(RolePermEnum.ADMIN_IDS);
        for (String permId : list) {
            SysRolePerm sysRolePerm = new SysRolePerm();
            sysRolePerm.setPermId(permId);
            sysRolePerm.setRoleId(rid);
            rolePermList.add(sysRolePerm);
        }
        return rolePermList;
    }

    private List<SysRolePerm> initBusinessRolePerm(String rid) {
        List<SysRolePerm> rolePermList = new ArrayList<>();
        List<String> list = RolePermEnum.list(RolePermEnum.BUSINESS_IDS);
        for (String permId : list) {
            SysRolePerm sysRolePerm = new SysRolePerm();
            sysRolePerm.setPermId(permId);
            sysRolePerm.setRoleId(rid);
            rolePermList.add(sysRolePerm);
        }
        return rolePermList;
    }

    private List<SysRolePerm> initLeaderRolePerm(String rid) {
        List<SysRolePerm> rolePermList = new ArrayList<>();
        List<String> list = RolePermEnum.list(RolePermEnum.LEADER_IDS);
        for (String permId : list) {
            SysRolePerm sysRolePerm = new SysRolePerm();
            sysRolePerm.setPermId(permId);
            sysRolePerm.setRoleId(rid);
            rolePermList.add(sysRolePerm);
        }
        return rolePermList;
    }

    private List<SysRolePerm> initFinanceRolePerm(String rid) {
        List<SysRolePerm> rolePermList = new ArrayList<>();
        List<String> list = RolePermEnum.list(RolePermEnum.FINANCE_IDS);
        for (String permId : list) {
            SysRolePerm sysRolePerm = new SysRolePerm();
            sysRolePerm.setPermId(permId);
            sysRolePerm.setRoleId(rid);
            rolePermList.add(sysRolePerm);
        }
        return rolePermList;
    }

    private List<SysRolePerm> initCreditRolePerm(String rid) {
        List<SysRolePerm> rolePermList = new ArrayList<>();
        List<String> list = RolePermEnum.list(RolePermEnum.CREDIT_IDS);
        for (String permId : list) {
            SysRolePerm sysRolePerm = new SysRolePerm();
            sysRolePerm.setPermId(permId);
            sysRolePerm.setRoleId(rid);
            rolePermList.add(sysRolePerm);
        }
        return rolePermList;
    }

    private List<SysRole> initRole() {
        List<SysRole> roleList = new ArrayList<>();
        List<String> roleNamesList = SysRoleEnum.list();
        for (String roleName : roleNamesList) {
            SysRole sysRole = new SysRole();
            if ("管理员".equals(roleName)) {
                sysRole.setIsAdmin(1);
            } else {
                sysRole.setIsAdmin(0);
            }
            sysRole.setIsPreset(1);
            sysRole.setRname(roleName);
            roleList.add(sysRole);
        }
        return roleList;
    }

}
