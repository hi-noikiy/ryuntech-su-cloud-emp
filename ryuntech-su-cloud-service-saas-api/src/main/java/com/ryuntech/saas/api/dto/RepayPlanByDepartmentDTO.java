package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class RepayPlanByDepartmentDTO {
    private String departmentName;
    private BigDecimal planAmount;
    private BigDecimal backedAmount;
    private BigDecimal surplusAmount;
    private BigDecimal overdueAmount;
}