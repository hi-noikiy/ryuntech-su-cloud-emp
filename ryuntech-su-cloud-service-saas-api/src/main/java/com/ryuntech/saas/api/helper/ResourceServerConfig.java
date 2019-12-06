package com.ryuntech.saas.api.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * @author antu
 * @date 2019-05-25
 */
@Slf4j
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    // 资源端不需要验证的url集合
    public static final String[] WHITELIST = {
            "/actuator/**",
            "/user/info/*",
            "/user/login",
            "/user/sendRegisterSms",
            "/user/checkRegisterSmsCode",
            "/company/listByUsername",
            "/company/choose",
            "/user/register",
            "/sms/*",
            "/storage/local/upload",
            "/*/out**" // 小程序端
    };

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("加载了 >> ResourceServerConfig...");
        http
                //允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
                .headers()
                .frameOptions().disable()

                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,
                        "/upload/**",
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.json",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs/**")
                .permitAll()
                .antMatchers(WHITELIST)
                .permitAll()

                .anyRequest()
                .authenticated()

                .and()
                .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl("https://wx.ryuntech.com:9999/api/auth/oauth/check_token");
        tokenService.setClientId("client");
        tokenService.setClientSecret("secret");

        resources.tokenServices(tokenService);
    }
}