package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.mapper.EmployeeMapper;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.service.IEmployeeService;
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
public class EmployeeServiceImpl extends BaseServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Override
    public Employee selectByEmployee(Employee employee) {
        if (StringUtils.isNotBlank(employee.getUserId())){
            queryWrapper.eq("user_id", employee.getUserId());
        }
        return baseMapper.selectOne(queryWrapper);
    }
}
