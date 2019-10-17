package com.ryuntech.saas.service.impl;

import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.mapper.EmployeeRoleMapper;
import com.ryuntech.saas.api.model.EmployeeRole;
import com.ryuntech.saas.api.service.IEmployeeRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 职员-角色关联表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-10-17
 */
@Service
public class EmployeeRoleServiceImpl extends BaseServiceImpl<EmployeeRoleMapper, EmployeeRole> implements IEmployeeRoleService {

}
