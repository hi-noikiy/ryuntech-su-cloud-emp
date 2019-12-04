package com.ryuntech.saas.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class EmployeeEditForm {
    private String companyId;

    private String employeeId;

    private String mobile;

    private String employeeName;

    private String departmentId;

    private String email;

    private String isCharger;

    private String dataType;

    private String dataDepartmentId;

    private String roleIds;
}
