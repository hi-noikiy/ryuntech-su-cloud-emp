package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.EmployeeDTO;
import com.ryuntech.saas.api.dto.EmployeeDetailDTO;
import com.ryuntech.saas.api.form.EmployeeEditForm;
import com.ryuntech.saas.api.form.EmployeeForm;
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

    /**
     * 根据用户id查询该用户所在的公司
     * @param userId
     * @return
     */
    List<Map<String, String>> selectCompanys(String userId);

    /**
     * 根据部门id集合查询集合下的所有员工id
     * @param departmentIdList
     * @return
     */
    List<String> queryEmployeeIds(List<String> departmentIdList);

    /**
     * 根据员工id集合查询对应的员工角色
     * @param departmentIdList
     * @return
     */
    List<Map<String, String>> queryRoleLimitEmployeeIds(List<String> employeeIdList);

    /**
     * 根据员工id集合查询集合下的所有员工列表
     * @param map
     * @param page
     * @return
     */
    IPage<Employee> queryListByLimitSearch(Map map, QueryPage page);


    Result<IPage<EmployeeDTO>> getPager(EmployeeForm employeeForm);

    EmployeeDetailDTO detail(String employeeId);

    boolean updateStatus(String emplyoeeId, String status) throws Exception;

    boolean edit(EmployeeEditForm employeeEditForm);
}
