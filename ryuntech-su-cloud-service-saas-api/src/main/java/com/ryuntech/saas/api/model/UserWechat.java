package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* <p>
    *
    * </p>
*
* @author antu
* @since 2019-10-15
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_user_wechat")
    public class UserWechat implements Serializable {

    private static final long serialVersionUID = 1L;

        @TableField("USER_ID")
    private String userId;

            @TableId("OPEN_ID")
    private String openId;

        @TableField("IMG_URL")
    private String imgUrl;

        @TableField("SESSION_KEY")
    private String sessionKey;

        @TableField("NICKNAME")
    private String nickname;

    @TableField(exist = false)
    private String token;


}
