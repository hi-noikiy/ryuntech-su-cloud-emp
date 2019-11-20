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
public class ApiTaxIllegalList {
    private ApiHeader apiHeader;
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        /**
         * 内部ID
         */
        private String Id;
        /**
         * 项目名发布时间
         */
        private String IllegalTime;
        /**
         * 案件性质
         */
        private String CaseNature;
        /**
         * 所属税务机关
         */
        private String TaxGov;
    }
}
