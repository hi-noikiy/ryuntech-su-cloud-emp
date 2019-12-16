package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class OverdueByCustomerDTO {
    private String customerName;
    private BigDecimal overdueAmount;
    private Integer overdueContractNum;
    private Integer maxOverdueDay;
}