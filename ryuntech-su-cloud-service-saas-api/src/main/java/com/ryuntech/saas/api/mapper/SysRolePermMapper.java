package com.ryuntech.saas.api.mapper;

import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.model.SysRolePerm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-09-12
 */
@Repository
public interface SysRolePermMapper extends IBaseMapper<SysRolePerm> {

    int deleteByRoleId(String roleId);

    int batchInsert(@Param("rolePermList") List<SysRolePerm> rolePermList);
}
