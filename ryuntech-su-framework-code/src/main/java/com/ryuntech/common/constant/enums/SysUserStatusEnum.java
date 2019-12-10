package com.ryuntech.common.constant.enums;

public enum SysUserStatusEnum {
    NORMAL(1, "可用"),
    UN_USED(0, "不可用");

    private int status;
    private String desc;

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    SysUserStatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
