package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.Employee;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author antu
 * @since 2019-10-15
 */
public interface IEmployeeService extends IBaseService<Employee> {
    /**
     * 根据员工获取对应的数据
     * @param employee
     * @return
     */
    Employee selectByEmployee(Employee employee);

    /**
     *
     * @return
     */
    Result<IPage<Employee>> selectListBySearch(Employee employee, QueryPage page);
}
