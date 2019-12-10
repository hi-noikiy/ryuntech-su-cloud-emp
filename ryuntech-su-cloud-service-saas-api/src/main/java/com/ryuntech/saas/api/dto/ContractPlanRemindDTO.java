package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 应收计划相关信息 服务实现类
 * </p>
 *
 * @author wanh
 * @since 2019-12-10
 */
@Data
@Accessors(chain = true)
public class ContractPlanRemindDTO {

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合同金额
     */
    private String contractAmount;

    /**
     * 回款金额
     */
    private String collectionAmount;

    /**
     * 计划总期数
     */
    private String totalPlanPeriodes;

    /**
     * 部门id
     */
    private String departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 职工编号
     */
    private String employeeId;

    /**
     * 职工姓名
     */
    private String name;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否是部门负责人
     */
    private Integer isCharger;

    /**
     * 计划回款金额
     */
    private String planAmount;

    /**
     * 计划剩余金额
     */
    private String backedAmount;

    /**
     * 计划已回金额
     */
    private String surplusAmount;

    /**
     * 计划回款时间
     */
    private Date planTime;

    /**
     * 计划期数
     */
    private String planPeriod;

}
