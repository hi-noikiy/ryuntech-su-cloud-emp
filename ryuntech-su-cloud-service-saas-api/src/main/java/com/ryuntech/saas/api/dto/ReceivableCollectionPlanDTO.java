package com.ryuntech.saas.api.dto;

import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class ReceivableCollectionPlanDTO extends BaseModel {

    /**
    * 计划金额
    * */
    private String planAmount;
    /**
     * 计划编号
     */
    private String planId;
    /**
     * 合同编号
     */
    private String contractId;
    /**
     * 已回款金额
     */
    private String backAmount;
    /**
     * 计划剩余金额
     */
    private String surplusAmount;
    /**
     * 备注
     */
    private String remakes;

    /**
     * 计划回款时间
     */
    private String planTime;
    /**
     * 回款状态(0逾期1已还款2未开始)
     */
    private String status;
}
