package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 用户微信信息表
    * </p>
*
* @author antu
* @since 2019-10-17
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_user_wechat")
    public class UserWechat extends BaseModel {

    private static final long serialVersionUID = 1L;

            /**
            * 关联用户表主键
            */
    private String userId;

            /**
            * 微信Union_id
            */
    private String unionId;

            /**
            * 小程序openid
            */
    private String miniprogramOpenid;

            /**
            * 公众号openid
            */
    private String gongzhonghaoOpenid;

            /**
            * 用户是否关注公众号 0-未关注 1-已关注
            */
    private Boolean subscribe;

            /**
            * 昵称
            */
    private String nickname;

            /**
            * 性别 1 男，2女，0未知
            */
    private Boolean sex;

            /**
            * 省
            */
    private String province;

            /**
            * 城市
            */
    private String city;

            /**
            * 国家
            */
    private String country;

            /**
            * 头像
            */
    private String headimgurl;

            /**
            * 用户的语言，简体中文为zh_CN
            */
    private String language;

            /**
            * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
            */
    private Integer subscribeTime;

            /**
            * 公众号运营者对粉丝的备注
            */
    private String remark;

            /**
            * 用户所在分组
            */
    private Integer groupid;

            /**
            * 用户关注的渠道来源
            */
    private String subscribeScene;

            /**
            * 用户被打上的标签ID列表
            */
    private String tagidList;

            /**
            * 二维码扫码场景
            */
    private String qrScene;

            /**
            * 二维码扫码场景描述
            */
    private String qrSceneStr;

            /**
            * 最后更新时间
            */
    private LocalDateTime updatedAt;

            /**
            * 创建时间
            */
    private LocalDateTime createdAt;


}
