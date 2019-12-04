package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SmsSend implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("sms_send_id")
    private String smsSendId;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 短信业务类型（1.注册2.登录3.找回密码4.找回密码)
     */
    @TableField("type")
    private Integer type;

    /**
     * 提交到短信平台的短信替换内容（JSON串）
     */
    @TableField("sms_code")
    private String smsCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "created_at")
    private Date createdAt;

}
