package com.ryuntech.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum SysRoleEnum {
    ADMIN("管理员"),
    BUSINESS("业务员"),
    LEADER("业务负责人"),
    FINANCE("财务"),
    CREDIT("信控");

    SysRoleEnum(String desc) {
        this.desc = desc;
    }

    private String desc;

    public String getDesc() {
        return desc;
    }

    public static List<String> list() {
        List<String> list = new ArrayList<>();
        for (SysRoleEnum sysRoleEnum : SysRoleEnum.values()) {
            list.add(sysRoleEnum.getDesc());

        }
        return list;
    }

    // 枚举时使用
    public static SysRoleEnum getByValue(String desc) {
        for (SysRoleEnum sysRoleEnum : values()) {
            if (sysRoleEnum.getDesc().equals(desc)) {
                return sysRoleEnum;
            }
        }
        return null;
    }
}