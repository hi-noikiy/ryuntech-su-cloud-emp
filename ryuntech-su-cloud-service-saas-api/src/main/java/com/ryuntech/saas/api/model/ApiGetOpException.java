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
public class ApiGetOpException extends BaseApi {

    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        /**
         * 列入经营异常名录原因
         */
        private String AddReason;
        /**
         * 列入日期
         */
        private String AddDate;
        /**
         * 移出经营异常名录原因
         */
        private String RomoveReason;
        /**
         * 移出日期
         */
        private String RemoveDate;
        /**
         * 作出决定机关
         */
        private String DecisionOffice;
        /**
         * 移除决定机关
         */
        private String RemoveDecisionOffice;
    }
}
