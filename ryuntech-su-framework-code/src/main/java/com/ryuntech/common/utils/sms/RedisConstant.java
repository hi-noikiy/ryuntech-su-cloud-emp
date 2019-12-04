package com.ryuntech.common.utils.sms;

/**
 * 命名用 英文冒号(:) 隔开，key不要太长或太短，尽量可读性高
 */
public class RedisConstant {

    // 系统配置key
    public static final String SYS_CONFIG = "sys:config";// [hash]

    // 登录时图形验证码存放redis的key的前缀
    public static final String PRE_LOGIN_CAPTCHA = "login:captcha:";// [string]
    public static final int PRE_LOGIN_CAPTCHA_EXPIRE = 5 * 60;// [string]

    // 根据token存储客户信息(user对象)
    public static final String PRE_LOGIN_USER = "login:user:";// [string]
    // 用户信息
    public static final String PRE_LOGIN_USER_TOKEN = "login:user:token:";// [string]
    // 权限信息
    public static final String PRE_LOGIN_USER_PERMISSION_URI = "login:user:permission:uri:";// [set]
    public static final String PRE_LOGIN_USER_PERMISSION_KEY = "login:user:permission:key:";// [set]
    public static final int LOGIN_TOKEN_EXPIRE = 30 * 60;// [string]

    // 登录错误次数，计时从最后一次登录错误开始
    public static final String PRE_LOGIN_ERROR_COUNT = "login:error:count:";// [string]
    public static final int PRE_LOGIN_ERROR_COUNT_EXPIRE = 2 * 60 * 60;// [string]

    // 短信验证码 例 sms:verification_code:{templateType}:{mobile}
    public static final String PRE_SMS_VERIFICATION_CODE = "sms:verification_code:";// [string]
    public static final int PRE_SMS_VERIFICATION_CODE_EXPIRE = 10 * 60;// [string]

    // 不同短信模板短信每分钟仅能发送一条 例 sms:exist:in:minute:{templateType}:{mobile}
    public static final String PRE_SMS_EXIST_IN_MINUTE = "sms:exist:in:minute:";// [string]
    public static final int PRE_SMS_EXIST_IN_MINUTE_EXPIRE = 1 * 60;// [string]

    // 注册，邮件内容中的验证码
    public static final String PRE_REGISTER_EMAIL_CODE = "email:register:";// [string]
    public static final int PRE_REGISTER_EMAIL_CODE_EXPIRE = 10 * 60;// [string]

    // 邮箱验证成功，标记允许5分鐘內注册操作
    public static final String PRE_ALLOW_REGISTER = "email:register:";// [string]
    public static final int PRE_ALLOW_REGISTER_EXPIRE = 5 * 60;// [string]

    // 找回密码，邮件内容中的验证码
    public static final String PRE_RESET_PASSWORD_EMAIL_CODE = "email:reset_password_code:";// [string]
    public static final int PRE_RESET_PASSWORD_EMAIL_CODE_EXPIRE = 10 * 60;// [string]

    // 邮箱验证成功，标记允许5分鐘內修改密码
    public static final String PRE_ALLOW_RESET_PASSWORD = "email:allow_reset_password:";// [string]
    public static final int PRE_ALLOW_RESET_PASSWORD_EXPIRE = 5 * 60;// [string]

    // 发送邮件每分钟仅一条 email:exist:in:minute:{email}
    public static final String PRE_EMAIL_EXIST_IN_MINUTE = "email:exist:in:minute:";// [string]
    public static final int PRE_EMAIL_EXIST_IN_MINUTE_EXPIRE = 1 * 60;// [string]

    // 事业部列表
    public static final String SYS_BUSINESS = "sys:business";// [set]

    // 防止频繁请求
    public static final String REQUEST_LIMIT = "request:limit";// [set]
    public static final int REQUEST_LIMIT_EXPIRE = 3;

    public static final String IMPORT_TASK_LOCK = "company:import:lock";
}
