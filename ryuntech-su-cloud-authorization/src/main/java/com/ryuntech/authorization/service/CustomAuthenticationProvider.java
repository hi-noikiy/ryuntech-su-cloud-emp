package com.ryuntech.authorization.service;

import com.ryuntech.authorization.entity.CompanyDetail;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

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
        String password = authentication.getCredentials().toString();
        CompanyDetail companyDetail = new CompanyDetail(username, password);
        return new UsernamePasswordAuthenticationToken(companyDetail, password, new ArrayList<>());
    }

    /**
     * 是否支持当前认证方式
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
