package com.ryuntech.saas.api.form;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* <p>
    * 接收前端回款信息
    * </p>
*
* @author antu
* @since 2019-09-27
*/
    @Data
    @Accessors(chain = true)
    public class ReceivableCollectionForm extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 回款编号（主键）
     */
    private String collectionId;

    /**
     * 客户编号
     */
    private String customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 回款金额
     */
    private String amount;

    /**
     * 回款账户
     */
    private String cardNo;

    /**
     * 回款方式
     */
    private String type;

    /**
     * 回款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    /**
     * 凭证类型
     */
    private String voucherType;

    /**
     * 凭证附件编码
     */
    @TableField("ATTACHMENT_CODE")
    private String attachmentCode;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 回款状态(0已作废，1已收款，2回款中)
     */
    private String status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 合同编号
     */
    private String contractId;

    /**
     * 计划编号
     */
    private String planId;

    /**
     * 应收合同应收未还
     */
    private String balanceAmount;

    /**
     * 应收合同已还款
     */
    private String collectionAmount;

}
