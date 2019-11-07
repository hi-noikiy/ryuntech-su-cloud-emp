package com.ryuntech.saas.api.helper.constant;

/**
 * @author EDZ
 */
public class PlanConstant {

//    STATUS	VARCHAR2(4)	Y	回款状态(0逾期1已还款2未开始3还款中)

    /**
     * 还款中
     */
    public static final String STARTING ="3";

    /**
     * 未开始
     */
    public static final String NOTSTARTED ="2";
    /**
     * 已还款
     */
    public static final String REIMBURSEMENT ="1";
    /**
     * 逾期
     */
    public static final String OVERDUED ="0";

}
