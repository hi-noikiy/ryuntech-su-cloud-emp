package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.EmployeeDTO;
import com.ryuntech.saas.api.dto.EmployeeDetailDTO;
import com.ryuntech.saas.api.form.EmployeeForm;
import com.ryuntech.saas.api.model.Employee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-10-15
 */
@Component
public interface EmployeeMapper extends IBaseMapper<Employee> {

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
    List<String> queryEmployeeIds(@Param("departmentIdList") List<String> departmentIdList);

    /**
     * 根据员工id集合查询对应的员工角色
     * @param
     * @return
     */
    List<Map<String, String>> queryRoleLimitEmployeeIds(@Param("employeeIdList") List<String> employeeIdList);

    /**
     * 根据员工id集合查询集合下的所有员工列表
     * @param page
     * @param map
     * @return
     */
    IPage<Employee> queryListByLimitSearch(@Param("pg") Page<Employee> page, @Param("map") Map map);

    List<EmployeeDTO> getPager(EmployeeForm employeeForm);

    EmployeeDetailDTO detail(String employeeId);

    int migrateToAnotherDept(@Param("oldDeptId") String oldDeptId, @Param("newDeptId") String newDeptId);
}
