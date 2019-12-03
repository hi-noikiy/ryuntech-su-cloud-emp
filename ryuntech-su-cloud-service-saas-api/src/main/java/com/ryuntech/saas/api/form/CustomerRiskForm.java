package com.ryuntech.saas.api.form;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class CustomerRiskForm {

    private String riskId;

    private String customerId;

    private String customerName;

    /**
     * 查询月份
     */
    private String riskTime;
}
