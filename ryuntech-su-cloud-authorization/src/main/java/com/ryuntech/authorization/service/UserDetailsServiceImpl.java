package com.ryuntech.authorization.service;

import com.ryuntech.authorization.entity.CompanyDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 加载用户数据
 *
 * @author antu
 * @date 2019-05-24
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CompanyDetail(username);
    }
}
