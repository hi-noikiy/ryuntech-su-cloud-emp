package com.ryuntech.saas.api.form;

import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class ContractRecordForm extends BaseModel {

    private String staffId;

    private String staffName;

    private String contractId;

    private String contractName;
//    提醒类型
    private String type;
}
