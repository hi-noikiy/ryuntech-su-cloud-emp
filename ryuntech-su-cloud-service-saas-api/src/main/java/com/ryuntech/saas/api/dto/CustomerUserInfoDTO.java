package com.ryuntech.saas.api.dto;

import com.ryuntech.saas.api.model.CustomerRisk;
import com.ryuntech.saas.api.model.ReceivableContract;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class CustomerUserInfoDTO {

    /**
     * 客户编号
     */
    private String customerId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 跟进人编号
     */
    private String staffId;

    /**
     * 跟进人姓名
     */
    private String staffName;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系人手机号
     */
    private String contactsPhone;

    /**
     * 城市编号
     */
    private String cityId;

    /**
     * 详细地址
     */
    private String address;


    /**
     * 总待收金额
     */
    private String allBalanceAmounts;
    /**
     * 总已回款
     */
    private String allCollectionAmount;
    /**
     * 总合同总额
     */
    private String allContractAmount;

    /**
     * 合同信息
     */
    List<ReceivableContract> receivableContractList;

    /**
     * 客户风险
     */
    List<CustomerRisk> customerRiskList;

    /**
     * 回款率
     */
    private String backRate;


}
