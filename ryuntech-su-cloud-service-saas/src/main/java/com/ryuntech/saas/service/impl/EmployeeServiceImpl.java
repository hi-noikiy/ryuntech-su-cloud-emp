package com.ryuntech.saas.service.impl;

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
import org.springframework.stereotype.Service;

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

    @Override
    public Employee selectByEmployee(Employee employee) {
        if (StringUtils.isNotBlank(employee.getUserId())){
            queryWrapper.eq("user_id", employee.getUserId());
        }
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Result<IPage<Employee>> selectListBySearch(Employee employee, QueryPage queryPage) {
        log.info(employee.toString());
        if (StringUtils.isNotBlank(employee.getDepartmentId())) {
            queryWrapper.eq("department_id",employee.getDepartmentId());
        }
        if (employee.getStatus() != null && StringUtils.isNotBlank(String.valueOf(employee.getStatus()))) {
            queryWrapper.eq("status",employee.getStatus());
        }
        Page<Employee> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        return super.pageList(queryWrapper, page);
    }
}
