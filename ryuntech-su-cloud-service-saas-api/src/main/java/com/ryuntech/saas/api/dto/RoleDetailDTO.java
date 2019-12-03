package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 角色详情, 包括角色id, 名称, 拥有的资源列表
 */
@Data
@Accessors(chain = true)
public class RoleDetailDTO {

    /**
     * 角色id
     */
    String roleId;

    /**
     * 角色名称
     */
    String roleName;

    /**
     * 角色拥有的资源列表
     */
    List<PermGroupDTO> permGroupList;

}
