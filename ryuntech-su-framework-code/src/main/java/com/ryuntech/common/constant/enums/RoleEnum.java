package com.ryuntech.common.constant.enums;

public enum RoleEnum {
    // 是否管理员
    ADMIN(1, "是"),
    UN_ADMIN(0, "不是"),
    // 是否预设
    PRE(1, "是"),
    UN_PRE(0, "不是");

    private int status;
    private String desc;

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    RoleEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
