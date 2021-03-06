package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;

@Data
@Accessors(chain = true)
public class EmployeeDTO {
    private String employeeId;

    private String concatRoleName;

    private String name;

    private String departmentName;

    private String mobile;

    private Integer status;

    private Integer dataType;

}
