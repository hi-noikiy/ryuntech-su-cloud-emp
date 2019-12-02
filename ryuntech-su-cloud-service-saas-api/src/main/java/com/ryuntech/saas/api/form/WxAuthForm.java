package com.ryuntech.saas.api.form;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class WxAuthForm {
    private String codeValue;
    private String userId;
    private String employeeId;
    private String nickname;
    private String sex;
    private String avatar;
    private String country;
    private String province;
    private String city;
}
