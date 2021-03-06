package com.ryuntech.saas.api.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 用于封装UserDetails的User扩展信息
 *
 * @author antu
 * @date 2019-05-24
 */
public class SctUser extends User {

    /**
     * 用户ID
     */
    @Getter
    private String id;

    public SctUser(String id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }
}
