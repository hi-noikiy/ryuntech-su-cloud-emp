package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应收计划提醒邮件模板 服务实现类
 * </p>
 *
 * @author wanh
 * @since 2019-12-06
 */
@Data
@Accessors(chain = true)
public class ContractPlanMessageRemindDTO {

    /**
     * 合同编号
     */
    private String contractId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 客户编号
     */
    private String customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合同金额
     */
    private String contractAmount;

    /**
     * 应收余额
     */
    private String balanceAmount;

    /**
     * 回款金额
     */
    private String collectionAmount;

    /**
     * 合同状态(0已逾期,1已完成，2执行中)
     */
    private String status;

    /**
     * 部门
     */
    private String department;

    /**
     * 负责人员工编号
     */
    private String staffId;

    /**
     * 负责人员工姓名
     */
    private String staffName;

    /**
     * 计划回款金额
     */
    private String planAmount;

    /**
     * 计划已回金额
     */
    private String backedAmount;

    /**
     * 计划回款时间
     */
    private String planTime;

    /**
     * 计划期数
     */
    private String planPeriod;

    /**
     * 逾期天数
     */
    private Integer overdueDays;

}
