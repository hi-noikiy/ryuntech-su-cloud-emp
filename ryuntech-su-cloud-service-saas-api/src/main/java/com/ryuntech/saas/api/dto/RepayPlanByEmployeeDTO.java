package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class RepayPlanByEmployeeDTO {
    private String employeeId;
    private String employeeName;
    private BigDecimal planAmount;
    private BigDecimal backedAmount;
    private BigDecimal surplusAmount;
    private BigDecimal overdueAmount;
    private Integer contractNum;
}