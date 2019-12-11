package com.ryuntech.saas.api.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class CustomerRiskDetailListDTO {

    private String customerName;
    private String customerId;
    private String balanceAmount;
    private String staffName;

    private List<RiskListDetail> riskListDetails;

    @Data
    @Accessors(chain = true)
    public static class RiskListDetail{

        private String riskType;
        private String riskLevel;
        private String riskName;
        private String riskSize;

        private List<RiskListDetail2> riskListDetails2;

        @Data
        @Accessors(chain = true)
        public static class RiskListDetail2{
            private String riskContent;
            private String riskCode;
            private String keyNo;
            private String riskTime;
        }
    }
}
