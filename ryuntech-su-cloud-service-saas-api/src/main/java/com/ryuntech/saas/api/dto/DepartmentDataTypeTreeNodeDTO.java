package com.ryuntech.saas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDataTypeTreeNodeDTO {
    /**
     * 员工Id
     */
    private String employeeId;
    /**
     * 员工姓名
     */
    private String employeeName;

    private List<DepartmetnTreeNodeDTO> departmetnTreeNodeDTO;

    public DepartmentDataTypeTreeNodeDTO(String employeeId, String employeeName) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    public DepartmentDataTypeTreeNodeDTO(List<DepartmetnTreeNodeDTO> departmetnTreeNodeDTO) {
        this.departmetnTreeNodeDTO = departmetnTreeNodeDTO;
    }

}
