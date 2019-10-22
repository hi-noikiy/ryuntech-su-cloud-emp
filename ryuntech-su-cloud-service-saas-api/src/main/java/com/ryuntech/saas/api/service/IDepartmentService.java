package com.ryuntech.saas.api.service;

import com.ryuntech.saas.api.model.Department;

import java.util.ArrayList;
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
public interface IDepartmentService extends IBaseService<Department> {
    /**
     *  返回部门组织树
     */
    List<Map> selectDepartmentTree(String company_id, String pid, ArrayList path);

    /**
     * 新增部门
     * @param department
     */
    void create(Department department);

    /**
     * 更新部门
     * @param department
     */
    void update(Department department);

    /**
     * 根据ID查询部门
     * @param departmentId
     * @return
     */
    Department findById(String departmentId);

}
