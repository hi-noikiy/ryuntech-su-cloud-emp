package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class OverdueByContractDTO {
    private String contractId;
    private String contractName;
    private String customerName;
    private BigDecimal overdueAmount;
    private BigDecimal maxOverdueDay;
    private String employeeName;
    private String departmentName;
}