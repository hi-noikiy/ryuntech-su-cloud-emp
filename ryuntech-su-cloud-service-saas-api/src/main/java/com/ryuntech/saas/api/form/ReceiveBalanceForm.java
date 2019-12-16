package com.ryuntech.saas.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ReceiveBalanceForm {
    private String departmentId;

    private String beginTime;

    private String endTime;
}
