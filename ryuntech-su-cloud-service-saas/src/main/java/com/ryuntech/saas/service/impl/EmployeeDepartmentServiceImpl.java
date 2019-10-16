package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.mapper.EmployeeDepartmentMapper;
import com.ryuntech.saas.api.model.EmployeeDepartment;
import com.ryuntech.saas.api.service.IEmployeeDepartmentService;
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
@Service
public class EmployeeDepartmentServiceImpl extends BaseServiceImpl<EmployeeDepartmentMapper, EmployeeDepartment> implements IEmployeeDepartmentService {

    @Override
    public EmployeeDepartment selectByEmployeeDepartment(EmployeeDepartment employeeDepartment) {
        if (StringUtils.isNotBlank(employeeDepartment.getEmployeeId())){
            queryWrapper.eq("employee_id",employeeDepartment.getEmployeeId());
        }
        return baseMapper.selectOne(queryWrapper);
    }
}
