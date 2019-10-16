package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.saas.api.model.EmployeeDepartment;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author antu
 * @since 2019-10-15
 */
public interface IEmployeeDepartmentService extends IBaseService<EmployeeDepartment> {

    /**
     * 根据员工部门对象获取数据
     * @param employeeDepartment
     * @return
     */
    EmployeeDepartment selectByEmployeeDepartment(EmployeeDepartment employeeDepartment);
}
