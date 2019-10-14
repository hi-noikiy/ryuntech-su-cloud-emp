package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class IndexDTO {

    private List<String> contractIdList;
    private String companyName;

    private Date startDate;

    private Date endDate;
}
