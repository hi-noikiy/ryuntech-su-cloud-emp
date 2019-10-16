package com.ryuntech.saas.api.dto;

import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 *
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class WeChatIndex extends BaseModel {
    private String employeeId;
    private String status;

    private Date startDate;

    private Date endDate;

}
