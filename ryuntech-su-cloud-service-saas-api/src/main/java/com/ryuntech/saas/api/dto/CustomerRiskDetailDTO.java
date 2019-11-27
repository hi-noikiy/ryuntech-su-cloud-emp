package com.ryuntech.saas.api.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class CustomerRiskDetailDTO {
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 待收款
     */
    private String balanceAmount;

    /**
     * 负责员工
     */
    private String staffName;

    /**
     * 详情列表
     */
    private List<CustomerRiskDetail2DTO> customerRiskDetail2DTOs;


    @Data
    @Accessors(chain = true)
    public class CustomerRiskDetail2DTO{
        private String riskType;
        private String tiskSize;
        private List<CustomerRiskDetail3DTO> customerRiskDetail3DTOs;

        @Data
        @Accessors(chain = true)
        public class CustomerRiskDetail3DTO{
            private String riskContent;
            private String riskTime;
        }
    }


}
