package com.ryuntech.saas.api.form;

import com.ryuntech.saas.api.model.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class DepartmentForm {
    private String deptId;
    private String deptName;
    private String parentId;

    public Department convertToDepartment() {
        Department dept = new Department();
        dept.setDepartmentId(deptId);
        dept.setDepartmentName(deptName);
        dept.setPid(parentId);
        dept.setUpdatedAt(new Date());
        return dept;
    }
}
