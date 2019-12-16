package com.ryuntech.saas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RepayPlanTotalDTO {
    private BigDecimal totalPlanAmount;
    private BigDecimal totalBackedAmount;
    private BigDecimal totalSurplusAmount;
}