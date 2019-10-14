package com.ryuntech.saas.api.model;

import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Index extends BaseModel {

    private String amounts;
    private String balanceAmounts;
    private String contractId;
}
