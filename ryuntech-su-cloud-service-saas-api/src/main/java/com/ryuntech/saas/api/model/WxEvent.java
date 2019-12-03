package com.ryuntech.saas.api.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WxEvent {

    /**
     * 消息来源用户标识
     */
    private String FromUserName;
    /**
     * 消息目的用户标识
     */
    private String ToUserName;
    /**
     * 消息类型(event或者text)
     */
    private String MsgType;

    /**
     * 时间类型
     */
    private String Event;

    /**
     * 消息内容
     */
    private String Content;
}
