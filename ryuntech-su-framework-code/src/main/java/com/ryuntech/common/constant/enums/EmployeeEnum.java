package com.ryuntech.common.constant.enums;

public enum EmployeeEnum {
    // 是否是部门负责人
    CHARGER(1, "是"),
    UN_CHARGER(0, "不是"),
    // 账号状态
    NORMAL(1, "正常"),
    UN_USED(0, "禁用");

    private int status;
    private String desc;

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    EmployeeEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
