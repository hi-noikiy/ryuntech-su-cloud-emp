package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReceiveBalanceContractAmount {
    private String customerId;
    private String employeeId;
    private String saleAmount;
}