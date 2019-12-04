package com.ryuntech.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum RolePermEnum {
    ADMIN_IDS("管理员"),
    BUSINESS_IDS("业务员"),
    LEADER_IDS("业务负责人"),
    FINANCE_IDS("财务"),
    CREDIT_IDS("信控");

    RolePermEnum(String desc) {
        this.desc = desc;
    }

    private String desc;

    public String getDesc() {
        return desc;
    }

    public static List<String> list(RolePermEnum rolePermEnum) {
        List<String> list = null;
        switch (rolePermEnum) {
            case ADMIN_IDS:
                list = new ArrayList<>();
                list.add("762194993925300001");
                list.add("762194993925300002");
                list.add("762194993925300003");
                list.add("762194993925300004");
                list.add("762194993925300005");
                list.add("762194993925300006");
                list.add("762194993925300007");
                list.add("762194993925300008");
                list.add("762194993925300009");
                list.add("762194993925300010");
                list.add("762194993925300011");
                list.add("762194993925300012");
                list.add("762194993925300013");
                list.add("762194993925300014");
                list.add("762194993925300015");
                list.add("762194993925300016");
                list.add("762194993925300017");
                list.add("762194993925300018");
                list.add("762194993925300019");
                list.add("762194993925300020");
                list.add("762194993925300021");
                list.add("762194993925300022");
                list.add("762194993925300023");
                list.add("762194993925300024");
                list.add("762194993925300025");
                list.add("762194993925300026");
                break;
            case BUSINESS_IDS:
                list = new ArrayList<>();
                list.add("762194993925300001");
                list.add("762194993925300003");
                list.add("762194993925300004");
                list.add("762194993925300005");
                list.add("762194993925300006");
                list.add("762194993925300007");
                list.add("762194993925300012");
                list.add("762194993925300013");
                list.add("762194993925300014");
                list.add("762194993925300015");
                break;
            case LEADER_IDS:
                list = new ArrayList<>();
                list.add("762194993925300001");
                list.add("762194993925300003");
                list.add("762194993925300004");
                list.add("762194993925300005");
                list.add("762194993925300006");
                list.add("762194993925300007");
                list.add("762194993925300012");
                list.add("762194993925300013");
                list.add("762194993925300014");
                list.add("762194993925300015");
                list.add("762194993925300016");
                list.add("762194993925300017");
                list.add("762194993925300018");
                list.add("762194993925300019");
                list.add("762194993925300020");
                list.add("762194993925300021");
                list.add("762194993925300022");
                break;
            case FINANCE_IDS:
                list = new ArrayList<>();
                list.add("762194993925300001");
                list.add("762194993925300003");
                list.add("762194993925300004");
                list.add("762194993925300005");
                list.add("762194993925300008");
                list.add("762194993925300009");
                list.add("762194993925300010");
                list.add("762194993925300011");
                list.add("762194993925300012");
                list.add("762194993925300020");
                list.add("762194993925300021");
                list.add("762194993925300022");
                break;
            case CREDIT_IDS:
                list = new ArrayList<>();
                list.add("762194993925300001");
                list.add("762194993925300003");
                list.add("762194993925300004");
                list.add("762194993925300005");
                list.add("762194993925300006");
                list.add("762194993925300007");
                list.add("762194993925300012");
                list.add("762194993925300013");
                list.add("762194993925300014");
                list.add("762194993925300015");
                list.add("762194993925300016");
                list.add("762194993925300017");
                list.add("762194993925300018");
                list.add("762194993925300019");
                list.add("762194993925300020");
                list.add("762194993925300021");
                list.add("762194993925300022");
                break;
        }
        return list;
    }
}