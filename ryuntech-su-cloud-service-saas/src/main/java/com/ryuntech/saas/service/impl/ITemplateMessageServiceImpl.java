package com.ryuntech.saas.service.impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ryuntech.common.utils.HttpUtils;
import com.ryuntech.saas.api.helper.HttpConstant;
import com.ryuntech.saas.api.model.ApiPrroper;
import com.ryuntech.saas.api.model.SendTemplateReq;
import com.ryuntech.saas.api.model.SendTemplateResponse;
import com.ryuntech.saas.api.service.ITemplateMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author EDZ
 */
@Slf4j
@Service
public class ITemplateMessageServiceImpl implements ITemplateMessageService {


    @Override
    public SendTemplateResponse sendTemplateMessage(String accessToken, SendTemplateReq weixinTemplate){
        String url = new StringBuffer(HttpConstant.SERVICEHOST).append("/cgi-bin/message/template/send?access_token=")
                .append(accessToken).toString();
        JSONObject jsonObject = packJsonMsg(weixinTemplate);
        log.info("jsonObject=="+jsonObject.toJSONString());
        String content = HttpUtils.httpsRequest(url, "POST", jsonObject.toJSONString());
        return  new Gson().fromJson(content, SendTemplateResponse.class);
    }



    public static JSONObject packJsonMsg(SendTemplateReq weixinTemplate) {
        JSONObject data = new JSONObject();
        try {
            JSONObject json = new JSONObject();
            JSONObject jsonFirst = new JSONObject();
            if (null!=weixinTemplate.getData().getFrist()){
                jsonFirst.put("value", weixinTemplate.getData().getFrist().getValue());
                jsonFirst.put("color", "#173177");
                json.put("first", jsonFirst);
            }

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", weixinTemplate.getData().getKeyword1().getValue());
            keyword1.put("color", "#173177");
            json.put("keyword1", keyword1);


            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", weixinTemplate.getData().getKeyword2().getValue());
            keyword2.put("color", "#173177");
            json.put("keyword2", keyword2);

            if (null!=weixinTemplate.getData().getKeyword3()){
                JSONObject keyword3 = new JSONObject();
                keyword2.put("value", weixinTemplate.getData().getKeyword3().getValue());
                keyword2.put("color", "#173177");
                json.put("keyword3", keyword3);
            }

            if (null!=weixinTemplate.getData().getRemake()){
                JSONObject remark = new JSONObject();
                remark.put("value", weixinTemplate.getData().getRemake().getValue());
                remark.put("color", "#173177");
                json.put("remark", remark);
            }
            if (null!=weixinTemplate.getMiniprograms()){
                JSONObject miniprograms = new JSONObject();
                miniprograms.put("appid",weixinTemplate.getMiniprograms().get("appid"));
                miniprograms.put("pagepath",weixinTemplate.getMiniprograms().get("pagepath"));
                data.put("miniprograms", miniprograms);
            }


            data.put("touser", weixinTemplate.getTouser());
            data.put("template_id", weixinTemplate.getTemplate_id());
            data.put("url", weixinTemplate.getUrl());
            data.put("topcolor", "#173177");
            data.put("data", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
