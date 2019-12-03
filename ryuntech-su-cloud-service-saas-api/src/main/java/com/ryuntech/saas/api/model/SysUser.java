package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.*;

/**
 * @author antu
 * @date 2019-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("sys_user_id")
    private String sysUserId;

    /**
     * 手机号码
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 登录密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户状态(0不可用1可用)
     */
    @TableField("status")
    private String status;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 微信UNION_ID（如果绑定了微信）
     */
    @TableField("union_id")
    private String unionId;

    /**
     * 微信的OPENID
     */
    @TableField("open_id")
    private String openId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "created_at")
    private Date createdAt;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "updated_at")
    private Date updatedAt;
}
