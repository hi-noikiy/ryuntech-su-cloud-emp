package com.ryuntech.saas.api.entity;

    import com.baomidou.mybatisplus.annotation.TableName;
    import java.time.LocalDateTime;
    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

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

    private LocalDateTime followupTime;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;


}
