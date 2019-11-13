package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 部门层级关系
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class IndexDepartmentDTO {

    /**
     * 所属部门Id
     */
    private String departmentId;

    /**
     * 所属部门名称
     */
    private String departmentName;

    /**
     * 子孙部门
     */
    private List<IndexDepartmentDTO> sonDepartment;
}
