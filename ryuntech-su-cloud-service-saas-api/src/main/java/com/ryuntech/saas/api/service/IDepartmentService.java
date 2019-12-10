package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.DepartmetnTreeNodeDTO;
import com.ryuntech.saas.api.form.DepartmentForm;
import com.ryuntech.saas.api.model.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author antu
 * @since 2019-10-15
 */
public interface IDepartmentService extends IBaseService<Department> {
    /**
     * 返回部门组织树
     */
    List<Map> selectDepartmentTree(String company_id, String pid, ArrayList path);

    /**
     * 新增部门
     *
     * @param department
     */
    void create(Department department);

    /**
     * 更新部门
     *
     * @param department
     */
    void update(Department department);

    /**
     * 根据ID查询部门
     *
     * @param departmentId
     * @return
     */
    Department findById(String departmentId);

    /**
     * 根据条件查询部门
     */
    Department selectOneByWhere(Wrapper queryWrapper);

    /**
     * 根据条件删除部门
     */
    @Override
    boolean remove(Wrapper wrapper);

    /**
     * 根据部门对象查询
     *
     * @param department
     * @return
     */
    List<Department> findByDepartment(Department department);

    List<DepartmetnTreeNodeDTO> getDepartmentTree();

    void edit(DepartmentForm form);

    void delete(String deptId);

    int migrateToAnotherDept(String oldDeptId, String newDeptId);

    Result getDataTypeDepartmentTree() throws Exception;

    List<String> getDataTypeDepartments() throws Exception;
}
