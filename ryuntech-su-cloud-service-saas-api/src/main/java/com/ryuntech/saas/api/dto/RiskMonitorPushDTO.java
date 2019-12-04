package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */

@Data
@Accessors(chain = true)
public class RiskMonitorPushDTO {

    /**
     * 客户编号
     */
    private String staffId;
    /**
     * 客户名称
     */
    private String staffName;

    /**
     * 风险条数
     */
    private Integer riskSize;
    /**
     * 公司条数
     */
    private Integer companySize;
}
