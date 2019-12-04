package com.ryuntech.saas.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class EmployeeForm {
    private String companyId;

    private String departmentId;

    private String status;

    private String keyWord;

    private Integer pageCode;

    private Integer pageSize;
}
