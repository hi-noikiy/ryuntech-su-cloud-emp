package com.ryuntech.saas.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import com.ryuntech.saas.api.model.AttachmentFile;
import com.ryuntech.saas.api.model.FollowupRecord;
import com.ryuntech.saas.api.model.ReceivableContract;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 输出到页面数据
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class ReceivableContractDTO extends BaseModel {

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date contractTime;

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
     * 状态集
     */
    private List<String> statusList;

    /**
     * 部门
     */
    private String department;
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;

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
     * 逾期计划
     */
    ReceivableContract receivableContract;
    /**
     * 跟进信息
     */
    List<FollowupRecord> followUpRecords;
    /**
     * 跟进的第一个信息
     */
    FollowupRecord followupRecord;
}
