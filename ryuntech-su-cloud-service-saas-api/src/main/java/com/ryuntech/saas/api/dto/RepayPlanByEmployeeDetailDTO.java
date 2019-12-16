package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class RepayPlanByEmployeeDetailDTO {
    private String contractName;
    private String planTime;
    private BigDecimal planAmount;
    private BigDecimal backedAmount;
    private BigDecimal surplusAmount;
    private Integer status;
    private Integer planPeriodes;
}