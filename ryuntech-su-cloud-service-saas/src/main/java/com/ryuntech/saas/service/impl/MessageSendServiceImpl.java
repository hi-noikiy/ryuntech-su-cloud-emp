package com.ryuntech.saas.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.ryuntech.common.constant.RedisConstant;
import com.ryuntech.common.constant.generator.IncrementIdGenerator;
import com.ryuntech.common.constant.generator.UniqueIdGenerator;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.common.utils.redis.JedisUtil;
import com.ryuntech.saas.api.dto.Sms;
import com.ryuntech.saas.api.dto.SmsResponse;
import com.ryuntech.saas.api.mapper.SmsSendMapper;
import com.ryuntech.saas.api.model.SmsSend;
import com.ryuntech.saas.api.service.MessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.ryuntech.saas.api.helper.constant.SmsConstants.*;


@Service
public class MessageSendServiceImpl implements MessageSendService {

    @Autowired
    SmsSendMapper smsSendMapper;

    @Autowired
    JedisUtil jedisUtil;

    @Override
    public SmsResponse sendSms(Sms sms) {
        DefaultProfile profile = DefaultProfile.getProfile(PROFILE, ACCESSKEYID, ACCESSSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(DOMAIN);
        request.setVersion(VERSION);
        request.setAction(ACTION);
        request.putQueryParameter("RegionId", REGIONID);
        request.putQueryParameter("PhoneNumbers", sms.getPhoneNumbers());
        request.putQueryParameter("SignName", SIGNNAME);
        request.putQueryParameter("TemplateCode", TEMPLATECODE);
        request.putQueryParameter("TemplateParam", sms.getContent());

        try {
            CommonResponse response = client.getCommonResponse(request);
            Gson gson = new Gson();
            //解析从微信服务器获得的openid和session_key;
            SmsResponse smsResponse = gson.fromJson(response.getData(), SmsResponse.class);
            return smsResponse;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void querySendDetails() {
        DefaultProfile profile = DefaultProfile.getProfile("default", "<accessKeyId>", "<accessSecret>");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("QuerySendDetails");
        request.putQueryParameter("RegionId", "default");
        request.putQueryParameter("PhoneNumber", "18518215883");
        request.putQueryParameter("SendDate", "20190813");
        request.putQueryParameter("PageSize", "1");
        request.putQueryParameter("CurrentPage", "10");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendBatchSms() {
        DefaultProfile profile = DefaultProfile.getProfile("default", "<accessKeyId>", "<accessSecret>");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendBatchSms");
        request.putQueryParameter("RegionId", "default");
        request.putQueryParameter("PhoneNumberJson", "{}");
        request.putQueryParameter("SignNameJson", "{}");
        request.putQueryParameter("TemplateCode", "SMS_172120896");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean send(Sms sms) throws Exception {
        sendCore(sms);

        // 短信验证码，记录有效期10分钟
        jedisUtil.setex(RedisConstant.PRE_SMS_VERIFICATION_CODE + sms.getType() + ":" + sms.getPhoneNumbers(), RedisConstant.PRE_SMS_VERIFICATION_CODE_EXPIRE, sms.getCode());

        // 当前号码，当前模板发送短信成功，60s内不允许再发同模板类型的短信
        jedisUtil.setex(RedisConstant.PRE_SMS_EXIST_IN_MINUTE + sms.getType() + ":" + sms.getPhoneNumbers(), RedisConstant.PRE_SMS_EXIST_IN_MINUTE_EXPIRE, "");

        UniqueIdGenerator uniqueIdGenerator = UniqueIdGenerator.getInstance(IncrementIdGenerator.getServiceId());
        SmsSend smsSend = new SmsSend();
        smsSend.setSmsSendId(uniqueIdGenerator.nextStrId());
        smsSend.setMobile(sms.getPhoneNumbers());
        smsSend.setType(sms.getType());
        smsSend.setSmsCode(sms.getCode());
        smsSend.setCreatedAt(new Date());
        return smsSendMapper.insert(smsSend) > 0;
    }

    private void sendCore(Sms sms) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile(PROFILE, ACCESSKEYID, ACCESSSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(DOMAIN);
        request.setVersion(VERSION);
        request.setAction(ACTION);
        request.putQueryParameter("RegionId", REGIONID);
        request.putQueryParameter("SignName", SIGNNAME);
        request.putQueryParameter("PhoneNumbers", sms.getPhoneNumbers());
        request.putQueryParameter("TemplateCode", sms.getTemplatecode());
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + sms.getCode() + "\"}");

        CommonResponse response = client.getCommonResponse(request);
        Gson gson = new Gson();
        //解析从微信服务器获得的openid和session_key;
        SmsResponse smsResponse = gson.fromJson(response.getData(), SmsResponse.class);
        if ("OK".equalsIgnoreCase(smsResponse.getCode())) {
            return;
        } else if ("isv.BUSINESS_LIMIT_CONTROL".equalsIgnoreCase(smsResponse.getCode())) {
            throw new Exception("短信发送过于频繁,支持1条/分钟，5条/小时 ，累计10条/天");
        } else {
            throw new Exception(smsResponse.getMessage());
        }
    }

    @Override
    public boolean checkSmsCode(int templateType, String mobile, String code) {
        if (!StringUtil.isMobile(mobile)) {
            return false;
        }

        if (!StringUtil.isNumber(code, 6)) {
            return false;
        }

        String v = jedisUtil.get(RedisConstant.PRE_SMS_VERIFICATION_CODE + templateType + ":" + mobile);
        if (!code.equals(v)) {
            return false;
        }

        return true;
    }
}
