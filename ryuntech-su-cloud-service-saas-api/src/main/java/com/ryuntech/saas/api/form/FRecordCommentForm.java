package com.ryuntech.saas.api.form;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class FRecordCommentForm {


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
     * 评论内容
     */
    private String commentContent;
}
