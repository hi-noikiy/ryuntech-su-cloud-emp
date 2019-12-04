package com.ryuntech.saas.api.dto;


import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 角色名对象, 编辑员工角色时获取角色列表用.
 *
 * @author luojbin
 * @version 1.0
 * @date 2019/12/4 10:49
 */
@Data
@Accessors(chain = true)
public class RoleNameDTO {

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;
}
