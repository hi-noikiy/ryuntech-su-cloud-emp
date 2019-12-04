package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class DepartmetnTreeNodeDTO {

    /**
     * 部门名称
     */
    private String name;
    /**
     * 部门id
     */
    private String deptId;
    /**
     * 下级部门列表
     */
    private List<DepartmetnTreeNodeDTO> subDept;

}
