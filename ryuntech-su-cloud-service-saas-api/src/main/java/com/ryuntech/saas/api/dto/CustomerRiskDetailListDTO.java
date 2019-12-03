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

        private String tiskType;
        private String tiskSize;

        private List<RiskListDetail2> riskListDetails2;

        @Data
        @Accessors(chain = true)
        public static class RiskListDetail2{
            private String riskContent;
            private String riskTime;
        }
    }
}
