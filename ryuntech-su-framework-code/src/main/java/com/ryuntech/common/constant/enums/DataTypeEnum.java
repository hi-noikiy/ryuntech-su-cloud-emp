package com.ryuntech.common.constant.enums;

public enum DataTypeEnum {
    // 数据权限（1=本人2=本部门及下属部门3=全部4=指定部门）
    SELF(1, "本人"),
    SELF_AND_SUBORDINATE(2, "本部门及下属部门"),
    ALL(3, "全部"),
    APPOINT(4, "指定部门");

    private int status;
    private String desc;

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    DataTypeEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    // 数据权限使用
    public static DataTypeEnum getByValue(int status) {
        for (DataTypeEnum dataTypeEnum : values()) {
            if (dataTypeEnum.getStatus() == status) {
                return dataTypeEnum;
            }
        }
        return null;
    }
}
