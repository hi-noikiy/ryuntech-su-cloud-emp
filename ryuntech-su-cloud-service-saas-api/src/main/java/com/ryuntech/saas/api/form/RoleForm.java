package com.ryuntech.saas.api.form;

import com.ryuntech.common.exception.RyunBizException;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.model.SysRole;
import com.ryuntech.saas.api.model.SysRolePerm;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class RoleForm {
    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 权限列表
     */
    private List<String> permList;

    /**
     * 获取 SysRole 对象
     */
    public List<SysRolePerm> convertToSysRolePermList(){
        if (StringUtil.isEmpty(roleId)) {
            throw new RyunBizException("角色id不能为空, 操作失败");
        }
        List<String> permIdList = this.getPermList();
        List<SysRolePerm> rolePermList = new ArrayList<>(permIdList.size());
        for (String permId : this.getPermList()) {
            SysRolePerm rp = new SysRolePerm();
            rp.setRoleId(roleId);
            rp.setPermId(permId);
            rolePermList.add(rp);
        }
        return rolePermList;
    }

    /**
     * 获取 SysRolePerm 关联对象列表
     */
    public SysRole convertToSysRole(){
        SysRole role = new SysRole();
        role.setRid(roleId);
        role.setRname(roleName);
        role.setUpdated(new Date());
        return role;
    }
}
