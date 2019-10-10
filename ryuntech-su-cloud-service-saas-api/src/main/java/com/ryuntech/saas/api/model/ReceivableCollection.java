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
    * 回款表
    * </p>
*
* @author antu
* @since 2019-09-27
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_receivable_collection")
    public class ReceivableCollection implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 客户编号
            */
            @TableId("COLLECTION_ID")
    private String collectionId;

            /**
            * 客户名称
            */
        @TableField("CUSTOMER_ID")
    private String customerId;

            /**
            * 客户名称
            */
        @TableField("CUSTOMER_NAME")
    private String customerName;

            /**
            * 回款金额
            */
        @TableField("AMOUNT")
    private String amount;

            /**
            * 回款账户
            */
        @TableField("CARD_NO")
    private String cardNo;

            /**
            * 回款方式
            */
        @TableField("TYPE")
    private String type;

            /**
            * 回款时间
            */
        @TableField("TIME")
    private LocalDateTime time;

            /**
            * 凭证类型
            */
        @TableField("VOUCHER_TYPE")
    private String voucherType;

            /**
            * 凭证附件地址
            */
        @TableField("URL")
    private String url;

            /**
            * 创建人
            */
        @TableField("CREATE_BY")
    private String createBy;

            /**
            * 创建时间
            */
        @TableField("CREATE_TIME")
    private LocalDateTime createTime;

            /**
            * 回款状态(0已作废，1已收款)
            */
        @TableField("STATUS")
    private String status;

            /**
            * 备注
            */
        @TableField("REMARKS")
    private String remarks;


}
