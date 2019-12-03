package com.ryuntech.authorization.controller;

import com.ryuntech.authorization.service.LoginService;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    private TokenStore tokenStore;

    @RequestMapping("/login")
    public Result login(String username, String password) {
        return loginService.login(username, password);
    }

    @RequestMapping("/logout")
    public Result logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StringUtils.isBlank(authHeader)) {
            return new Result(CommonEnums.LOGOUT_ERROR);
        }
        String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, "").trim();
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(tokenValue);
        if (oAuth2AccessToken == null || StringUtils.isBlank(oAuth2AccessToken.getValue())) {
            return new Result(CommonEnums.LOGOUT_ERROR);
        }
        tokenStore.removeAccessToken(oAuth2AccessToken);
        return new Result();
    }

}
