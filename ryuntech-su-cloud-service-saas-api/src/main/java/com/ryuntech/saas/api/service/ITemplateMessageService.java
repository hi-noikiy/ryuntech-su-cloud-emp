package com.ryuntech.saas.api.service;

import com.ryuntech.saas.api.model.SendTemplateReq;
import com.ryuntech.saas.api.model.SendTemplateResponse;

/**
 * @author EDZ
 */
public interface ITemplateMessageService {

    /**
     * 微信发送模板消息
     * @param accessToken
     * @param weixinTemplate
     * @return
     * @throws Exception
     */
    SendTemplateResponse sendTemplateMessage(String accessToken, SendTemplateReq weixinTemplate) throws Exception;
}
