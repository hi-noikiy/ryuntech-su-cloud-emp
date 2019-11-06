package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.saas.api.model.Employee;

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
     *
     * @return
     */
    IPage<Employee> selectListBySearch(Map param, QueryPage page);

    /**
     * 根据用户id查询该用户所在的公司
     * @param userId
     * @return
     */
    List<Map<String, String>> selectCompanys(String userId);
}
