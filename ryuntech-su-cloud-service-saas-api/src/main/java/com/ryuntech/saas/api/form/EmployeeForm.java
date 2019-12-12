package com.ryuntech.saas.api.form;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmployeeForm {
    private String companyId;

    private String departmentId;

    private String status;

    private String keyWord;

    private Integer pageCode;

    private Integer pageSize;

    private String employeeId;

    private String email;
}
