package com.ryuntech.saas.api.dto;


import com.ryuntech.common.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Sms extends BaseModel {
    /*  request.putQueryParameter("RegionId", "default");
         request.putQueryParameter("PhoneNumbers", "18518215883");
         request.putQueryParameter("SignName", "睿云");
         request.putQueryParameter("TemplateCode", "SMS_172120896");*/
    private String regionId;
    private String phoneNumbers;
    private String signName;
    private String content;
    private String code;
    // 短信业务类型（1.注册2.登录3.找回密码)
    private Integer type;
    private String templatecode;

    public Sms(String phoneNumbers, String templatecode, Integer type, String code) {
        this.phoneNumbers = phoneNumbers;
        this.templatecode = templatecode;
        this.type = type;
        this.code = code;
    }
}
