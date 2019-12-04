package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.PermGroupDTO;
import com.ryuntech.saas.api.dto.RoleDetailDTO;
import com.ryuntech.saas.api.dto.RoleInfoDTO;
import com.ryuntech.saas.api.dto.RoleNameDTO;
import com.ryuntech.saas.api.form.RoleForm;
import com.ryuntech.saas.api.model.SysRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author antu
 * @since 2019-09-12
 */
public interface ISysRoleService extends IBaseService<SysRole> {
    Result<IPage<SysRole>> pageList(SysRole sysRole, QueryPage queryPage);
    List<String> getRoleIdsByUserId(String userId);


    boolean checkRidsContainRval(List<String> rids, String rval);

    List<RoleInfoDTO> getRoleInfoList();

    List<PermGroupDTO> getAllResources();

    RoleDetailDTO getRoleDetail(String roleId);

    List<RoleNameDTO> getNameList();

    void edit(RoleForm roleForm);
}
