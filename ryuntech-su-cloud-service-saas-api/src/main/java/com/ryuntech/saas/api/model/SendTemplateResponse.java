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
public class SendTemplateResponse {
    /**
     * 错误码
     * 0 ok
     * 40037 template_id不正确
     * 41028 form_id不正确，或者过期
     * 41029 form_id已被使用
     * 41030 page不正确
     * 45009 接口调用超过限额（目前默认每个帐号日调用限额为100万）
     */
    private int errcode;
    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 响应主键
     */
    private String msgid;


    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer("WeixinResponse[msgid=");
        buf.append(msgid)
                .append(",errcode=").append(errcode)
                .append(",errmsg=").append(errmsg)
                .append("]");
        return buf.toString();
    }
}
