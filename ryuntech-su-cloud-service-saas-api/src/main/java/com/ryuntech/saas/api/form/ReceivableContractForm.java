package com.ryuntech.saas.api.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import com.ryuntech.saas.api.dto.ReceivableCollectionPlanDTO;
import com.ryuntech.saas.api.model.AttachmentFile;
import com.ryuntech.saas.api.model.FollowupRecord;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 接受前台参数
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class ReceivableContractFrom extends BaseModel {
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
     * 合同日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime contractTime;

    /**
     * 合同金额
     */
    private String contractAmount;

    /**
     * 应收余额
     */
    private String balanceAmount;

    /**
     * 回款余额
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
     * 合同编码
     */
    private String contractCode;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String contactsPhone;

    /**
     * 负责人员工编号
     */
    private String staffId;

    /**
     * 负责人员工姓名
     */
    private String staffName;

    /**
     * 附件编码
     */
    private String attachmentCode;

    /**
     * 上传的文件路径
     */
    private List<AttachmentFile> files;

    /*
     * 还款计划
     * */
    List<ReceivableCollectionPlanDTO> receivableCollectionPlanDTOs;
    /**
     * 跟进信息
     */
    List<FollowupRecord> followUpRecords;
    /**
     * 跟进的第一个信息
     */
    FollowupRecord followupRecord;
}
