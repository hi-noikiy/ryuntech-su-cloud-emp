package com.ryuntech.common.constant.enums;

public enum CompanyEnum {
    // 是否删除
    DEL(1, "是"),
    UN_DEL(0, "不是"),
    // 是否企查查
    QI_CHA_CHA(1, "是"),
    UN_QI_CHA_CHA(0, "不是");

    private int status;
    private String desc;

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    CompanyEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
