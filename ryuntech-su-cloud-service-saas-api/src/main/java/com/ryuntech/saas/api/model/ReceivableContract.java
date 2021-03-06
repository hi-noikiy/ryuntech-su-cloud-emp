package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
* <p>
    * 应收合同表
    * </p>
*
* @author antu
* @since 2019-09-27
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_receivable_contract")
    public class ReceivableContract extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 合同编号
     */
    @TableId("CONTRACT_ID")
    private String contractId;

    /**
     * 合同名称
     */
    @TableField("CONTRACT_NAME")
    private String contractName;

    /**
     * 客户编号
     */
    @TableField("CUSTOMER_ID")
    private String customerId;

    /**
     * 客户名称
     */
    @TableField("CUSTOMER_NAME")
    private String customerName;

    /**
     * 合同日期
     */
    @TableField("CONTRACT_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date contractTime;

    /**
     * 合同金额
     */
    @TableField("CONTRACT_AMOUNT")
    private String contractAmount;

    /**
     * 待还款
     */
    @TableField("BALANCE_AMOUNT")
    private String balanceAmount;

    /**
     * 已还款
     */
    @TableField("COLLECTION_AMOUNT")
    private String collectionAmount;

    /**
     * 合同状态(0已逾期,1已完成，2执行中)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 部门
     */
    @TableField("DEPARTMENT")
    private String department;

    /**
     * 合同编码
     */
    @TableField("CONTRACT_CODE")
    private String contractCode;

    /**
     * 联系人
     */
    @TableField("CONTACTS")
    private String contacts;

    /**
     * 联系电话
     */
    @TableField("CONTACTS_PHONE")
    private String contactsPhone;

    /**
     * 负责人员工编号
     */
    @TableField("STAFF_ID")
    private String staffId;

    /**
     * 负责人员工姓名
     */
    @TableField("STAFF_NAME")
    private String staffName;

    /**
     * 附件编码
     */
    @TableField("ATTACHMENT_CODE")
    private String attachmentCode;

    /**
     * 计划总期数
     */
    @TableField("TOTAL_PLAN_PERIODES")
    private String totalPlanPeriodes;

    @TableField(exist = false)
    private String companyName;

    @TableField(exist = false)
    private List<String> statusList;

    @TableField(exist = false)
    private List<ReceivableCollectionPlan> receivableCollectionPlans;

    /**
     * 逾期计划
     */
    @TableField(exist = false)
    private ReceivableCollectionPlan receivableCollectionPlan;

    @Override
    public boolean equals(Object obj) {
        if (this==obj){
            return true;
        }
        if (obj==null){
            return false;
        }
        if (getClass()!=obj.getClass()){
            return false;
        }
        ReceivableContract other = (ReceivableContract) obj;
        if (other.getContractId().equals(this.getContractId())){
            return true;
        }

        return true;
    }

}
