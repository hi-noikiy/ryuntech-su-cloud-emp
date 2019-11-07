package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.model.ReceivableContract;

import java.util.List;
import java.util.Map;

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
     * 根据员工获取对应的员工列表数据
     * @param employee
     * @return
     */
    List<Employee> selectByEmployeeList(Employee employee);

    /**
     *
     * @return
     */
    IPage<Employee> selectListBySearch(Map param, QueryPage page);

    /**
     * 分页查询员工数据
     * @param employee
     * @param queryPage
     * @return
     */
    Result<IPage<Employee>> selectPageList(Employee employee, QueryPage queryPage);
}
