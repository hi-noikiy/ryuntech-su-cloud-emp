package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
    * 应收计划逾期跟进提醒
    * </p>
*
* @author antu
* @since 2019-12-03
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_plan_overdue_remind")
    public class PlanOverdueRemind implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 主键
            */
            @TableId("OVERDUE_ID")
    private String overdueId;

            /**
            * 公司编号
            */
        @TableField("COMPANY_ID")
    private String companyId;

            /**
            * 是否发送至跟进人（0表示否，1表示是）
            */
        @TableField("FOLLOW_PERSON")
    private String followPerson;

            /**
            * 是否发送至部门负责人（0表示否，1表示是）
            */
        @TableField("DEPART_HEAD")
    private String departHead;

            /**
            * 以邮件形式（0表示否，1表示是）
            */
        @TableField("EMAIL_TYPE")
    private String emailType;

            /**
            * 以公众号形式（0表示否，1表示是）
            */
        @TableField("WECHAT_TYPE")
    private String wechatType;

            /**
            * 间隔提醒天数
            */
        @TableField("INTERVAL_Day")
    private Integer intervalDay;

            /**
            * 创建时间
            */
        @TableField("CREATE_TIME")
    private Date createTime;

            /**
            * 修改时间
            */
        @TableField("UPDATE_TIME")
    private Date updateTime;


}
