package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class SysUserDTO {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 公司名
     */
    private String companyName;


    /**
     * 头像
     */
    private String avatar;

    /**
     * 姓名
     */
    private String name;
}
