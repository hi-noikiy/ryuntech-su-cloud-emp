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

/**
* <p>
    * 反馈信息表
    * </p>
*
* @author antu
* @since 2019-11-14
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_feedback")
    public class FeedBack extends BaseModel {

    private static final long serialVersionUID = 1L;

            /**
            * 反馈编号
            */
            @TableId("FEEDBACK_ID")
    private String feedbackId;

            /**
            * 反馈内容
            */
        @TableField("FEEDBACK_CONTENT")
    private String feedbackContent;

            /**
            * 反馈类型
            */
        @TableField("FEEDBACK_TYPE")
    private String feedbackType;

            /**
            * 附件编码
            */
        @TableField("ATTACHMENT_CODE")
    private String attachmentCode;

            /**
            * 更新时间
            */
            @TableField("UPDATED_AT")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

            /**
            * 创建时间
            */
            @TableField("CREATED_AT")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;


}
