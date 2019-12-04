package com.ryuntech.saas.api.mapper;

import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.model.SysPerm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-09-12
 */
@Repository
public interface SysPermMapper extends IBaseMapper<SysPerm> {
    /**
     * @param roleId
     * @return
     */
    List<SysPerm> getPermsByRoleId(@Param("roleId") String roleId);

    /**
     * @param userId
     * @return
     */
    List<SysPerm> getPermsByUserId(@Param("userId") String userId);

    List<SysPerm> queryByIdListAndCompanyId(@Param("newPermIdList") List<String> newPermIdList);
}
