package com.ryuntech.common.utils;

import com.google.gson.Gson;
import com.ryuntech.common.constant.RedisConstant;
import com.ryuntech.common.model.CurrentUser;
import com.ryuntech.common.utils.redis.JedisUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SystemTool {
    public static String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer", "").trim();
    }

    public static CurrentUser currentUser(JedisUtil jedisUtil) {
        Object u = jedisUtil.get(RedisConstant.PRE_CURRENT_USER + getToken());
        try {
            return new Gson().fromJson(String.valueOf(u), CurrentUser.class);
        } catch (Exception e) {
            return null;
        }
    }
}
