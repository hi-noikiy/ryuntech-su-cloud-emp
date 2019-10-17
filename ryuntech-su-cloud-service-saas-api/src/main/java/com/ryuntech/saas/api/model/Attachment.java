package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 附件表
    * </p>
*
* @author antu
* @since 2019-10-17
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_attachment")
    public class Attachment extends BaseModel {

    private static final long serialVersionUID = 1L;

            /**
            * 附件主键
            */
            @TableId("ID")
    private String id;

            /**
            * 附件编码
            */
        @TableField("ATTACHMENT_CODE")
    private String attachmentCode;

            /**
            * 附件地址
            */
        @TableField("ATTACHMENT_URL")
    private String attachmentUrl;

            /**
            * 附件类型
            */
        @TableField("ATTACHMENT_TYPE")
    private String attachmentType;

            /**
            * 附件类型
            */
        @TableField("STATUS")
    private String status;

            /**
            * 最后更新时间
            */
        @TableField("UPDATED_AT")
    private LocalDateTime updatedAt;

            /**
            * 创建时间
            */
        @TableField("CREATED_AT")
    private LocalDateTime createdAt;


}
