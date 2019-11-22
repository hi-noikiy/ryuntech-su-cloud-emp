package com.ryuntech.saas.api.form;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class CustomerRiskForm {

    private String riskId;

    private String customerId;

    private String customerName;

    private String riskTime;
}
