package com.ryuntech.authorization.controller;

import com.ryuntech.authorization.api.SysUserFeign;
import com.ryuntech.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class SysUserController implements SysUserFeign {

    @Autowired
    TokenStore tokenStore;

    @RequestMapping("/logout")
    @Override
    public Result logout(String token) {
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        tokenStore.removeAccessToken(oAuth2AccessToken);
        return new Result();
    }

}
