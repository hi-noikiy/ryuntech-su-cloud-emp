package com.ryuntech.saas.api.model;


import com.ryuntech.common.model.BaseApi;
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
public class ApiGetLandMortgageList extends BaseApi {
 
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String Id;
        /**
         * 地址
         */
        private String Address;
        /**
         * 行政区
         */
        private String AdministrativeArea;
        /**
         * 抵押面积（公顷）
         */
        private String MortgageAcreage;
        /**
         * 抵押土地用途
         */
        private String MortgagePurpose;
        /**
         * 开始日期
         */
        private String StartDate;
        /**
         * 结束日期
         */
        private String EndDate;
    }
}
