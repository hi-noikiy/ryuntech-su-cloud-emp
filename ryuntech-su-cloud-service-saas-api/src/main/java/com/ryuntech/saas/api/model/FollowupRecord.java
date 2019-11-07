package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
* <p>
    * 跟进表
    * </p>
*
* @author antu
* @since 2019-10-24
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_followup_record")
    public class FollowupRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String followupId;

    /**
     * 预计回款金额
     */
    private String estimateAmount;
    /**
     * 合同编号
     */
    private String contractId;
    /**
     * 合同名称
     */
    private String contractName;

            /**
            * 跟进内容
            */
    private String content;

            /**
            * 跟进人编号
            */
    private String staffId;

            /**
            * 跟进人姓名
            */
    private String staffName;

            /**
            * 附件编码
            */
    private String attachmentCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date followupTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date estimateTime;


}
