package com.ryuntech.saas.api.dto;


import com.ryuntech.saas.api.model.SysPerm;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 角色基本信息, 包含角色名及拥有的资源描述
 *
 * @author luojbin
 * @version 1.0
 * @date 2019/12/3 17:27
 */
@Data
@Accessors(chain = true)
public class RoleInfoDTO {

    public RoleInfoDTO(RoleDetailDTO roleDetail) {
        this.roleId = roleDetail.getRoleId();
        this.roleName = roleDetail.getRoleName();
        StringBuilder builder = new StringBuilder();
        for (PermGroupDTO group : roleDetail.getPermGroupList()) {
            for (SysPerm perm : group.getSysPermList()) {
                builder.append(perm.getResName()).append(",");
            }
        }
        if (builder.length() > 0) {
            this.resources = builder.substring(0, builder.length() - 1);
        } else {
            this.resources = "";
        }
    }

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色资源描述
     */
    private String resources;

}
