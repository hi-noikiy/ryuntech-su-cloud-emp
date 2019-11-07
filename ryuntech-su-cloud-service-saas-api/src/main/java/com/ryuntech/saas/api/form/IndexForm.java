package com.ryuntech.saas.api.form;

import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class IndexForm extends BaseModel {

    private List<String> contractIdList;
    private String companyName;

    private String employeeId;

    private Date startDate;

    private Date endDate;
}
