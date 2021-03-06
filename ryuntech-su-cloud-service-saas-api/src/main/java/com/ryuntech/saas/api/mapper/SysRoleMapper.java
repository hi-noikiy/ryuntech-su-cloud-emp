package com.ryuntech.saas.api.mapper;

import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.PermGroupDTO;
import com.ryuntech.saas.api.dto.RoleDetailDTO;
import com.ryuntech.saas.api.dto.RoleNameDTO;
import com.ryuntech.saas.api.model.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-09-12
 */
@Component
public interface SysRoleMapper extends IBaseMapper<SysRole> {

    /**
     * 根据userid查询权限
     * @param userId
     * @return
     */
    List<String> getRoleIdsByUserId(@Param("userId") String userId);


    /**
     * 根据userid查询角色
     * @param id
     * @return
     */
    List<SysRole> selectRoleById(@Param("id") String id);


    /**
     *
     * @param rids
     * @param rval
     * @return
     */
    Boolean checkRidsContainRval(List<String> rids, String rval);

    List<RoleDetailDTO> getRoleDetailList(String companyId);

    List<PermGroupDTO> getPermGroup();

    RoleDetailDTO getRoleDetailByCompanyIdAndRoleId(@Param("companyId") String companyId, @Param("roleId") String roleId);

    List<RoleNameDTO> getNameList(@Param("companyId") String companyId);
}
