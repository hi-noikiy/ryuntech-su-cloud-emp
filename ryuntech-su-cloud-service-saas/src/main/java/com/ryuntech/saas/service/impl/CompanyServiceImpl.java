package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.ryuntech.authorization.api.SysUserFeign;
import com.ryuntech.common.constant.RedisConstant;
import com.ryuntech.common.model.CurrentUser;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.redis.JedisUtil;
import com.ryuntech.saas.api.dto.LoginConpanyDTO;
import com.ryuntech.saas.api.dto.PermUriDTO;
import com.ryuntech.saas.api.mapper.CompanyMapper;
import com.ryuntech.saas.api.mapper.EmployeeMapper;
import com.ryuntech.saas.api.mapper.SysPermMapper;
import com.ryuntech.saas.api.model.Company;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.service.ICompanyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-10-17
 */
@Service
public class CompanyServiceImpl extends BaseServiceImpl<CompanyMapper, Company> implements ICompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private SysPermMapper sysPermMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SysUserFeign sysUserFeign;

    @Autowired
    private JedisUtil jedisUtil;

    @Override
    public Company selectByCompany(Company company) {
        if (StringUtils.isNotBlank(company.getName())) {
            queryWrapper.eq("name", company.getName());
        }
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<LoginConpanyDTO> listBySysUserId(String sysUserId) {
        return companyMapper.listBySysUserId(sysUserId);
    }

    @Override
    public boolean choose(String companyId, String token) {
        Result result = sysUserFeign.getSysUserId(token);
        String sysUserId = String.valueOf(result.getData());

        // 加载当前用户的权限key及uri
        // 1.查询出用户的角色对应的perm集合
        List<PermUriDTO> permUriList = sysPermMapper.getPermUri(companyId, sysUserId);
        List<String> uriList = permUriList.stream().map(p -> p.getUri()).collect(Collectors.toList());
        Set<String> uris = new HashSet<>(uriList);

        List<String> keyList = permUriList.stream().map(p -> p.getResKey()).distinct().collect(Collectors.toList());
        Set<String> keys = new HashSet<>(keyList);

        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id", companyId);
        queryWrapper.eq("sys_user_id", sysUserId);
        Employee employee = employeeMapper.selectOne(queryWrapper);

        CurrentUser currentUser = new CurrentUser(sysUserId, companyId, employee.getEmployeeId(), employee.getName(), employee.getDataType(), employee.getDataDepartmentId(), uris, keys);

        Map<String, String> map = new HashMap<>();
        map.put(companyId, new Gson().toJson(currentUser));
        jedisUtil.hmset(RedisConstant.PRE_LOGIN_USER + token, map);
        return true;
    }

}
