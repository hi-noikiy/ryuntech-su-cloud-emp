package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    *
    * </p>
*
* @author antu
* @since 2019-09-29
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_receivable_collection_plan")
    public class ReceivableCollectionPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("PLAN_ID")
    private String planId;

    /**
     * 计划金额
     */
    @TableField("PLAN_AMOUNT")
    private String planAmount;

    /**
     * 备注
     */
    @TableField("REMAKES")
    private String remakes;

    /**
     * 回款状态(0逾期1已还款2未开始)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 合同编号
     */
    @TableField("CONTRACT_ID")
    private String contractId;

    /**
     * 计划回款时间
     */
    @TableField("PLAN_TIME")
    private LocalDateTime planTime;


}
