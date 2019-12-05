package com.ryuntech.authorization.service;

import com.ryuntech.authorization.entity.LoginUserDetail;
import com.ryuntech.common.constant.enums.CommonEnums;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义身份认证验证组件
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        LoginUserDetail loginUserDetail = (LoginUserDetail) userDetailsService.loadUserByUsername(username);
        String password = authentication.getCredentials().toString();

        if (!bCryptPasswordEncoder.matches(password, loginUserDetail.getPassword())) {
            throw new BadCredentialsException(CommonEnums.LOGIN_ERROR.getMsg());
        }

        // 添加用户逻辑
        Set<String> keys = new HashSet<>();
        Set<String> uris = new HashSet<>();
        uris.add("uri1");
        uris.add("uri2");
        keys.add("key1");
        keys.add("key2");

        List<GrantedAuthority> list = new ArrayList<>();
        loginUserDetail.setAuthorities(new HashSet(list));

        loginUserDetail.setUris(uris);
        loginUserDetail.setKeys(keys);
        return new UsernamePasswordAuthenticationToken(loginUserDetail, password, new ArrayList<>());
    }

    /**
     * 是否支持当前认证方式
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
