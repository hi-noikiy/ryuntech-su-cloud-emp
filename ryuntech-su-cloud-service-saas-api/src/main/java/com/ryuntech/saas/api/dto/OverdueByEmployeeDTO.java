package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class OverdueByEmployeeDTO {
    private String employeeName;
    private BigDecimal overdueAmount;
    private Integer overdueContractNum;
    private Integer maxOverdueDay;
    private String departmentName;
}