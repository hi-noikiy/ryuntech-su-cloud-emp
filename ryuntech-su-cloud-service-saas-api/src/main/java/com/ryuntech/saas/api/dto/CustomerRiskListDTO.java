package com.ryuntech.saas.api.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class CustomerRiskListDTO {
    private String riskTime;

    private String riskLength;

    List<OnCompany> onCompanyList;

    String falgLength;

    @Data
    @Accessors(chain = true)
    public static class OnCompany{
        private String customerName;
        private String industrialandcommercial;
        private String industrialandLength;
        private String actionatlaw;
        private String actionatlawLength;
        private String operatingrisk;
        private String operatingriskLength;
    }
}
