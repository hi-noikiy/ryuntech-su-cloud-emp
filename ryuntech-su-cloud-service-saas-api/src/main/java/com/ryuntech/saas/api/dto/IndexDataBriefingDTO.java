package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 首页数据简报返回数据
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class IndexDataBriefingDTO {

    /**
     * 应收未还款
     */
    private String noRepayment;

    /**
     * 本月回款
     */
    private String backMoney;

    /**
     * 本月销售额
     */
    private String salesVolume;
}
