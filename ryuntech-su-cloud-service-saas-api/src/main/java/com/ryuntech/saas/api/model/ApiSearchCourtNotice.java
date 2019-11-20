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
public class ApiSearchCourtNotice {


    private ApiHeader apiHeader;
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String Defendantlist;
        private String Executegov;
        private String Prosecutorlist;
        private String LianDate;
        private String CaseReason;
        private String Id;
        private String CaseNo;
    }
}
