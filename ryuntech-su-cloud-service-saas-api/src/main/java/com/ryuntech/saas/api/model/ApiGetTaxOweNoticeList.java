package com.ryuntech.saas.api.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author EDZ
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApiGetTaxOweNoticeList {
    private ApiHeader apiHeader;
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String Id;
        private String Title;
        private String Amount;
        private String NewAmount;
        private String PublishDate;
        private String IssuedBy;
    }
}
