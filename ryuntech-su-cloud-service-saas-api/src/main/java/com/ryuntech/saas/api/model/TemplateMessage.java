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
public class TemplateMessage {

    /**
     * 用户openid
     */
    private String touser;

    /**
     * 模板消息ID
     */
    private String template_id;

    /**
     * 详情跳转页面
     */
    private String url;

    /**
     * 模板数据封装实体
     */
    private DoctorReplyMsgData data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DoctorReplyMsgData getData() {
        return data;
    }

    public void setData(DoctorReplyMsgData data) {
        this.data = data;
    }
}
