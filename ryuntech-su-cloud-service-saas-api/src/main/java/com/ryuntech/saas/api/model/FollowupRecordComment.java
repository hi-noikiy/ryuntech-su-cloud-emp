package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 跟进评论表
    * </p>
*
* @author antu
* @since 2019-11-15
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_followup_record_comment")
    public class FollowupRecordComment implements Serializable {

    private static final long serialVersionUID = 1L;

    private String commentId;

            /**
            * 跟进主键
            */
    private String followupId;

            /**
            * 评论人编号
            */
    private String staffId;

            /**
            * 评论人姓名
            */
    private String staffName;

            /**
            * 附件内容
            */
    private String commentContent;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

            /**
            * 评论时间
            */
    private LocalDateTime commentTime;


}
