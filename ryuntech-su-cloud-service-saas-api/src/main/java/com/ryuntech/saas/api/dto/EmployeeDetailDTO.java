package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmployeeDetailDTO {
    private String employeeId;

    private String mobile;

    private String name;

    private String departmentId;

    private String isCharger;

    private String email;

    private String dataType;

    private String roleIds;
}
