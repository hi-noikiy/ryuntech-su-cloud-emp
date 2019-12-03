package com.ryuntech.saas.api.helper;

/**
 * @author EDZ
 */
public class HttpConstant {
    /**
     * 微信host
     */
    public static final String SERVICEHOST = "https://api.weixin.qq.com";
    /**
     * 小程序授权链接
     */
    public static final String JSCODE2SESSION = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 小程序获取token链接
     */
    public static final String TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    /**
     * 获取uuid
     */
    public static final String INFOUNIONID="https://api.weixin.qq.com/cgi-bin/user/info";
}
