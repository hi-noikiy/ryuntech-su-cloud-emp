package com.ryuntech.saas.api.helper.constant;

/**
 * @author EDZ
 */
public class ReceivableContractConstants {

//      `STATUS` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '合同状态(0已逾期,1已完成，2执行中)',

    /**
     * 2执行中
     */
    public static final String NOTSTARTED ="2";
    /**
     * 1已完成
     */
    public static final String REIMBURSEMENT ="1";
    /**
     * 0已逾期
     */
    public static final String OVERDUED ="0";

}
