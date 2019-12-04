package com.ryuntech.saas.api.form;

import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class CustomerUserInfoForm extends BaseModel {

    private String customerId;

    private String customerName;

    private String contacts;

    private String contactsPhone;

    private String staffId;

    private String staffName;

    private String department;

    private String companyName;

    private String companyId;

    private String address;

    private String cityId;

    private String provinceId;

    private String districtId;

    /**
     * 是否被监控 0 否 1被监控
     */
    private Integer isRisk;
}
