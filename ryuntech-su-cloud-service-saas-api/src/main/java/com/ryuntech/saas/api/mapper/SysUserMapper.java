package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author antu
 * @date 2019-05-22
 */

public interface SysUserMapper  extends IBaseMapper<SysUser> {
//    selectUserRoleById

    /**
     * 查询用户信息，包括权限信息
     * @param user
     * @return
     */
    SysUser selectUserRoleById(@Param("user") SysUser user);


    /**
     * 分页查看用户信息
     * @param page
     * @param user
     * @return
     */
    IPage<SysUser> selectUsersRoleById(@Param("pg") Page<SysUser> page, @Param("user") SysUser user);

    /**
     * 获取用户id和用户名列表，用于搜索选择
     * @param user
     * @return
     */
    List<SysUser> selectUserMap(@Param("user") SysUser user);
}
