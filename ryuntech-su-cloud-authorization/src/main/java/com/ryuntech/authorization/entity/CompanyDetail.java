/*
package com.ryuntech.authorization.entity;*/
package com.ryuntech.authorization.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public class CompanyDetail implements UserDetails, Serializable {

    private String companyId;
    private String uuid; // 对应用户ID

    private Collection<? extends GrantedAuthority> authorities;

    public CompanyDetail(String companyId, String uuid) {
        this.companyId = companyId;
        this.uuid = uuid;
    }

    public CompanyDetail(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return this.uuid;
    }

    @Override
    public String getUsername() {
        return this.companyId;
    }

    public void setUsername(String username) {
        this.companyId = username;
    }

    public void setPassword(String password) {
        this.uuid = password;
    }


    //账号是否过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //账号是否锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //账号凭证是否未过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

