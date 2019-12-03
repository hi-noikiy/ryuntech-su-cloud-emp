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
public class SendTemplateReq {
    /**
     * 接收者（用户）的 openid
     */
    private String touser;
    /**
     * 所需下发的模板消息的id
     */
    private String template_id;
    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。
     * 该字段不填则模板无跳转。
     */
    private String page;
    /**
     * 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
     */
    private String form_id;
    /**
     * 模板内容
     * 不填则下发空模板。具体格式请参考示例。
     */
    private DoctorReplyMsgData data;
    /**
     * 模板内容json数据
     */
    private String jsonContent;

    /**
     * 模板需要放大的关键词
     * 不填则默认无放大
     */
    private String emphasis_keyword;
    /**
     * 点击后跳转的详情
     */
    private String url;
}
