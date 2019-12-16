package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ReceiveBalanceByEmployeeDTO {

    private String employeeName;

    private String departmentName;

    private BigDecimal saleAmount;

    private BigDecimal receiveBalance;

    private BigDecimal repayAmount ;
}
