package com.ryuntech.common.constant.enums;

public enum  SmsEnum {
    // 短信业务类型（1.注册2.登录3.找回密码)
    REGISTER(1, "注册"),
    LOGIN(0, "登录"),
    FIND_PASSWORD(0, "找回密码");

    private int status;
    private String desc;

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    SmsEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
