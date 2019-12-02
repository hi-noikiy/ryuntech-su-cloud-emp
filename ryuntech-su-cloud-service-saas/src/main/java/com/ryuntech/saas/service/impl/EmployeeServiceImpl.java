package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.mapper.EmployeeMapper;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
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

    @Override
    public Employee selectByEmployee(Employee employee) {
        if (StringUtils.isNotBlank(employee.getUserId())){
            queryWrapper.eq("user_id", employee.getUserId());
        }

        if (StringUtils.isNotBlank(employee.getEmployeeId())){
            queryWrapper.eq("employee_id", employee.getEmployeeId());
        }

        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Employee> selectByEmployeeList(Employee employee) {
        queryWrapper=new QueryWrapper<>();
        if (StringUtils.isNotBlank(employee.getUserId())){
            queryWrapper.eq("user_id", employee.getUserId());
        }
        if (StringUtils.isNotBlank(employee.getEmployeeId())){
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
            qw.eq("department_id",param.get("departmentId"));
        }
        if (param.containsKey("status")) {
            qw.eq("status",param.get("status"));
        }
        if (param.containsKey("employeeId")) {
            qw.eq("employee_id",param.get("employeeId"));
        }
        if (param.containsKey("companyId")) {
            qw.eq("company_id",param.get("companyId"));
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
        return super.pageList(queryWrapper,page);
    }
}
