package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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

            @TableId("PLAN_ID")
    private String planId;

        @TableField("PLAN_AMOUNT")
    private String planAmount;

        @TableField("REMAKES")
    private String remakes;

        @TableField("STATUS")
    private String status;

        @TableField("CONTRACT_ID")
    private String contractId;


}
