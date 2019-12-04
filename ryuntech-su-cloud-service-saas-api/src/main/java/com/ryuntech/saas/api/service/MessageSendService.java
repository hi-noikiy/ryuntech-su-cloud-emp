package com.ryuntech.saas.api.service;


import com.ryuntech.saas.api.dto.Sms;
import com.ryuntech.saas.api.dto.SmsResponse;

/**
 * @author EDZ
 * 用户登录名称 msg@1746465591239783.onaliyun.com
 * AccessKey ID LTAI7bXgddnt5tUv
 * AccessKeySecret NRiktAo46MpyemRj1Houeh94BhrMH0
 */
public interface MessageSendService {
    /**
     * 发送短信
     * @param sms
     * @return
     */
    SmsResponse sendSms(Sms sms);

    /**
     * 查询短信详情
     */
    void querySendDetails();

    /**
     * 批量发送短信
     */
    void sendBatchSms();

    boolean send(Sms sms) throws Exception;
    boolean checkSmsCode(int templateType, String mobile, String code);
}
