package com.ryuntech.common.utils;

import com.google.gson.Gson;
import com.ryuntech.common.constant.RedisConstant;
import com.ryuntech.common.model.CurrentUser;
import com.ryuntech.common.utils.redis.JedisUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class SystemTool {
    public static String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer", "").trim();
    }

    public static CurrentUser currentUser(JedisUtil jedisUtil, String companyId) {
        Object u = jedisUtil.hget(RedisConstant.PRE_LOGIN_USER + getToken(), companyId);
        try {
            return new Gson().fromJson(String.valueOf(u), CurrentUser.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static Set<String> uris(JedisUtil jedisUtil, String companyId) {
        CurrentUser currentUser = currentUser(jedisUtil, companyId);
        return currentUser == null ? null : currentUser.getUris();
    }

    public static Set<String> keys(JedisUtil jedisUtil, String companyId) {
        CurrentUser currentUser = currentUser(jedisUtil, companyId);
        return currentUser == null ? null : currentUser.getKeys();
    }

}
