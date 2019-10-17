package com.ryuntech.saas.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WeChatIndex {


    /**
     * 总待收金额
     */
    private String balanceAmounts;
    /**
     * 本月新增应收
     */
    private String contractAmounts;
    /**
     * 本月回款
     */
    private String collectionAmounts;


    /**
     * 逾期未收款数量
     */
    private Integer overdueNumber;

    /**
     * 逾期未收款金额
     */
    private String overdueSum;


    /**
     * 七天内到期的数量
     */
    private Integer expireNumber;

    /**
     * 七天内到期的金额
     */
    private String expireSum;


    /**
     * 本月预计回款数量
     */
    private Integer monthNumber;

    /**
     * 本月预计回款金额
     */
    private String monthSum;
}
