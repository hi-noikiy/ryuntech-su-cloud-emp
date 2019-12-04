package com.ryuntech.common.constant;

/**
 * 命名用 英文冒号(:) 隔开，key不要太长或太短，尽量可读性高
 */
public class RedisConstant {
    // 短信验证码 例 sms:verification_code:{type}:{mobile}
    public static final String PRE_SMS_VERIFICATION_CODE = "sms:verification_code:";// [string]
    public static final int PRE_SMS_VERIFICATION_CODE_EXPIRE = 5 * 60;// [string]

    // 不同短信模板短信每分钟仅能发送一条 例 sms:exist:in:minute:{type}:{mobile}
    public static final String PRE_SMS_EXIST_IN_MINUTE = "sms:exist:in:minute:";// [string]
    public static final int PRE_SMS_EXIST_IN_MINUTE_EXPIRE = 1 * 60;// [string]

    // 验证码验证成功，标记允许5分鐘內修改密码
    public static final String PRE_ALLOW_REGISTER = "email:allow_register:";// [string]
    public static final int PRE_ALLOW_REGISTER_EXPIRE = 5 * 60;// [string]
}
