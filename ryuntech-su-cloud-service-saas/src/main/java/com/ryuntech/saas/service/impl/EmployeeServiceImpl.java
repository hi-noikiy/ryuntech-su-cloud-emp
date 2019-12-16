package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ryuntech.common.constant.Global;
import com.ryuntech.common.constant.enums.EmployeeEnum;
import com.ryuntech.common.constant.enums.SysUserStatusEnum;
import com.ryuntech.common.constant.generator.IncrementIdGenerator;
import com.ryuntech.common.constant.generator.UniqueIdGenerator;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.dto.EmployeeDTO;
import com.ryuntech.saas.api.dto.EmployeeDetailDTO;
import com.ryuntech.saas.api.form.EmployeeEditForm;
import com.ryuntech.saas.api.form.EmployeeForm;
import com.ryuntech.saas.api.mapper.*;
import com.ryuntech.saas.api.model.Company;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.model.SysUserRole;
import com.ryuntech.saas.api.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-10-15
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends BaseServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public Employee selectByEmployee(Employee employee) {
        if (StringUtils.isNotBlank(employee.getSysUserId())) {
            queryWrapper.eq("user_id", employee.getSysUserId());
        }

        if (StringUtils.isNotBlank(employee.getEmployeeId())) {
            queryWrapper.eq("employee_id", employee.getEmployeeId());
        }

        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Employee> selectByEmployeeList(Employee employee) {
        queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(employee.getSysUserId())) {
            queryWrapper.eq("sys_user_id", employee.getSysUserId());
        }
        if (StringUtils.isNotBlank(employee.getEmployeeId())) {
            queryWrapper.eq("employee_id", employee.getEmployeeId());
        }
        return employeeMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<Employee> selectListBySearch(Map param, QueryPage queryPage) {
        log.info(param.toString());
        QueryWrapper qw = new QueryWrapper();
        if (param.containsKey("departmentId")) {
            // todo: 找出部门下的所有部门
            qw.eq("department_id", param.get("departmentId"));
        }
        if (param.containsKey("status")) {
            qw.eq("status", param.get("status"));
        }
        if (param.containsKey("employeeId")) {
            qw.eq("employee_id", param.get("employeeId"));
        }
        if (param.containsKey("companyId")) {
            qw.eq("company_id", param.get("companyId"));
        }
        // 关键字搜索
        if (param.containsKey("keyword") && StringUtils.isNotBlank((String) param.get("keyword"))) {
            String keyword = String.valueOf(param.get("keyword"));
//            qw.and(i -> i.like("mobile",keyword).or().like("name",keyword));
        }
        Page<Employee> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        return m.selectPage(page, queryWrapper);
    }

    @Override
    public List<Map<String, String>> selectCompanys(String userId) {
        return employeeMapper.selectCompanys(userId);
    }

    @Override
    public List<String> queryEmployeeIds(List<String> departmentIdList) {
        return employeeMapper.queryEmployeeIds(departmentIdList);
    }

    @Override
    public List<Map<String, String>> queryRoleLimitEmployeeIds(List<String> employeeIdList) {
        return employeeMapper.queryRoleLimitEmployeeIds(employeeIdList);
    }

    @Override
    public IPage<Employee> queryListByLimitSearch(Map map, QueryPage queryPage) {
        Page<Employee> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
//        if (StringUtils.isNotBlank((String) map.get("name"))) {
//            queryWrapper.eq("name", map.get("name"));
//        }
//        if (map.get("employeeIdList") != null) {
//            queryWrapper.eq("employeeIdList", map.get("employeeIdList"));
//        }
        return super.page(page, queryWrapper);
    }

    @Override
    public Result<IPage<Employee>> selectPageList(Employee employee, QueryPage queryPage) {
        Page<Employee> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (StringUtils.isNotBlank(employee.getEmployeeId())) {
            queryWrapper.eq("employee_id", employee.getEmployeeId());
        }
        if (StringUtils.isNotBlank(employee.getName())) {
            queryWrapper.eq("name", employee.getName());
        }
        return super.pageList(queryWrapper, page);
    }

    @Override
    public Result<IPage<EmployeeDTO>> getPager(EmployeeForm employeeForm) {
        PageHelper.startPage(employeeForm.getPageCode(), employeeForm.getPageSize());
        List<EmployeeDTO> list = employeeMapper.getPager(employeeForm);
        PageInfo<EmployeeDTO> pageInfo = new PageInfo(list);

        IPage<EmployeeDTO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(pageInfo.getTotal());
        page.setSize(pageInfo.getSize());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageNum());
        return new Result<>(page);
    }

    @Override
    public EmployeeDetailDTO detail(String employeeId) {
        return employeeMapper.detail(employeeId);
    }

    @Override
    public boolean updateStatus(String emplyoeeId, String status) throws Exception {

        Employee employee = employeeMapper.selectById(emplyoeeId);
        if (employee == null || employee.getStatus() == Integer.parseInt(status)) {
            throw new Exception("数据异常");
        }

        employee.setEmployeeId(emplyoeeId);
        employee.setStatus(Integer.parseInt(status));
        employee.setUpdatedAt(new Date());
        return employeeMapper.updateById(employee) > 0;
    }

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean edit(EmployeeEditForm employeeEditForm) {
        UniqueIdGenerator uniqueIdGenerator = UniqueIdGenerator.getInstance(IncrementIdGenerator.getServiceId());
        Date time = new Date();
        if (employeeEditForm.getEmployeeId() == null) {
            // 添加用户表数据
            SysUser sysUser = new SysUser();
            sysUser.setSysUserId(uniqueIdGenerator.nextStrId());
            sysUser.setUsername(employeeEditForm.getMobile());
            sysUser.setMobile(employeeEditForm.getMobile());
            // TODO 添加员工时，暂用这个密码
            sysUser.setPassword(new BCryptPasswordEncoder().encode(Global.REGISTER_PASSWORD));
            //   sysUser.setPassword(new BCryptPasswordEncoder().encode(password));
            sysUser.setStatus(String.valueOf(SysUserStatusEnum.NORMAL.getStatus()));
            sysUser.setCreatedAt(time);
            sysUser.setUpdatedAt(time);
            sysUserMapper.insert(sysUser);

            Company company = companyMapper.selectById(employeeEditForm.getCompanyId());

            // 添加员工表数据
            Employee employee = new Employee();
            employee.setCompanyId(employeeEditForm.getCompanyId());
            employee.setCompanyName(company.getName());
            employee.setEmployeeId(uniqueIdGenerator.nextStrId());
            employee.setSysUserId(sysUser.getSysUserId());
            employee.setName(employeeEditForm.getEmployeeName());
            employee.setDepartmentId(employeeEditForm.getDepartmentId());
            employee.setEmail(employeeEditForm.getEmail());
            employee.setIsCharger(employee.getIsCharger());
            employee.setDataType(Integer.parseInt(employeeEditForm.getDataType()));
            employee.setStatus(EmployeeEnum.NORMAL.getStatus());
            employee.setCreatedAt(time);
            employee.setUpdatedAt(time);
            employeeMapper.insert(employee);

            String[] roles = employeeEditForm.getRoleIds().split(",");
            for (String rId : roles) {
                if (!StringUtil.isNumber(rId)) {
                    continue;
                }

                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setEmployeeId(employee.getEmployeeId());
                sysUserRole.setRoleId(rId);
                sysUserRoleMapper.insert(sysUserRole);
            }
        } else {
            // 更新员工
            Employee employee = new Employee();
            employee.setEmployeeId(employeeEditForm.getEmployeeId());
            employee.setName(employeeEditForm.getEmployeeName());
            employee.setDepartmentId(employeeEditForm.getDepartmentId());
            employee.setIsCharger(employeeEditForm.getIsCharger());
            employee.setEmail(employeeEditForm.getEmail());
            employee.setDataType(Integer.parseInt(employeeEditForm.getDataType()));
            employee.setDataDepartmentId(employeeEditForm.getDataDepartmentId());
            employee.setUpdatedAt(time);
            employeeMapper.updateById(employee);

            // 更新员工角色信息
            //1.清空当前员工角色信息
            sysUserRoleMapper.deleteByEmployeeId(employeeEditForm.getEmployeeId());

            //2.添加当前员工角色信息
            String[] roles = employeeEditForm.getRoleIds().split(",");
            for (String rId : roles) {
                if (!StringUtil.isNumber(rId)) {
                    continue;
                }

                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setEmployeeId(employeeEditForm.getEmployeeId());
                sysUserRole.setRoleId(rId);
                sysUserRoleMapper.insert(sysUserRole);
            }
        }

        return true;
    }
}
