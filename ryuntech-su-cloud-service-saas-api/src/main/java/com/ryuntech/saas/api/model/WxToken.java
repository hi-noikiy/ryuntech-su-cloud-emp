package com.ryuntech.saas.api.model;


import lombok.Data;

/**
 * @author EDZ
 */
@Data
public class WxToken {
    /**
     * 微信加密签名
     */
    private String signature;
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 随机数
     */
    private String nonce;

    /**
     * 随机字符串
     */
    private String echostr;
}
