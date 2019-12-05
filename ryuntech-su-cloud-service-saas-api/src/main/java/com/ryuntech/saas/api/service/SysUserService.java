package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.saas.api.dto.SysUserDTO;
import com.ryuntech.saas.api.form.SysUserForm;
import com.ryuntech.saas.api.model.SysUser;

import java.util.List;

/**
 * @author antu
 * @date 2019-05-22
 */
public interface SysUserService extends IBaseService<SysUser> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    SysUser findByName(String username);


    /**
     * 条件查询用户
     *
     * @param user
     * @return
     */
    List<SysUser> list(SysUser user);

    /**
     * 新增用户
     *
     * @param user
     */
    void create(SysUser user);

    /**
     * 更新用户
     *
     * @param user
     */
    void update(SysUser user);

    /**
     * 查询用户信息
     *
     * @param user
     * @return
     */
    SysUser selectUserRoleById(SysUser user);

    /**
     * 分页查看用户信息
     *
     * @param user
     * @param queryPage
     * @return
     */
    IPage<SysUser> selectUsersRoleById(SysUser user, QueryPage queryPage);

    /**
     * 获取用户ID和用户名的列表，用于搜索选择
     *
     * @param user
     * @return
     */
    List<SysUser> selectUserMap(SysUser user);

    /**
     * 通过指定用户查询
     *
     * @param user
     * @return
     */
    SysUser selectByUser(SysUser user);


    /**
     * 通过指定用户查询
     *
     * @param userDTO
     * @return
     */
    SysUser selectByUserDTO(SysUserDTO userDTO);


    /**
     * 注册
     * @param companyName
     * @param employeeName
     * @param mobile
     * @param password
     * @return
     * @throws Exception
     */
    boolean saveRegister(String companyName, String employeeName, String mobile, String password) throws Exception;

    boolean sendRegisterSms(String mobile) throws Exception;
}
