package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
import java.util.*;

/**
 * @author antu
 * @date 2019-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser extends BaseModel implements UserDetails {

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

    private String status;

    /**
     * 公司名
     */
    @TableField(exist = false)
    private String companyName;

    /**
     * 用户所在公司职工编号
     */
    @TableField(exist = false)
    private String employeeId;

    /**
     * 头像
     */
    private String avatar;
    @TableField(exist = false)
    private String rval;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * 微信union_id（如果绑定了微信）
     */
    private String unionId;

    private String openId;


    /**
     * 手机验证码
     */
    private String vcode;


    @TableField(exist = false)
    /**用户所有角色值，在管理后台显示用户的角色*/
    private List<SysRole> roleList = new ArrayList<>();
    @TableField(exist = false)
    /**用户所有角色值，用于shiro做角色权限的判断*/
    private Set<Auth> roles = new HashSet<>();

    @TableField(exist = false)
    /**用户所有权限值，用于shiro做资源权限的判断*/
    private Set<Auth> perms = new HashSet<>();

    @TableField(exist = false)
    private List<? extends GrantedAuthority> authorities;

    @TableField(exist = false)
    private List<Employee> employees;


    public void setGrantedAuthorities(List<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
