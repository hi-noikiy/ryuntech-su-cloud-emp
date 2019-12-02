package com.ryuntech.saas.service.impl;

import com.google.gson.Gson;
import com.ryuntech.common.utils.HttpUtils;
import com.ryuntech.saas.api.helper.HttpConstant;
import com.ryuntech.saas.api.model.SendTemplateReq;
import com.ryuntech.saas.api.model.SendTemplateResponse;
import com.ryuntech.saas.api.service.ITemplateMessageService;
import org.springframework.stereotype.Service;

/**
 * @author EDZ
 */
@Service
public class ITemplateMessageServiceImpl implements ITemplateMessageService {

    @Override
    public SendTemplateResponse sendTemplateMessage(String accessToken, SendTemplateReq weixinTemplate) throws Exception {
        String url = new StringBuffer(HttpConstant.SERVICEHOST).append("/cgi-bin/message/template/send?access_token=")
                .append(accessToken).toString();
        String content = HttpUtils.Get(url);
        Gson gson = new Gson();
        SendTemplateResponse sendTemplateResponse = gson.fromJson(content, SendTemplateResponse.class);
        return sendTemplateResponse;
    }
}
