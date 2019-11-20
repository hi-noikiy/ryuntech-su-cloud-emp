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
public class ApiGetGsAdministrativePenaltyList {

    private ApiHeader apiHeader;
    private List<Result> results;


    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        /**
         * 行政处罚决定书文号
         */
        private String DocNo;
        /**
         * 违法行为类型
         */
        private String PenaltyType;
        /**
         * 行政处罚决定机关名称
         */
        private String OfficeName;
        /**
         * 行政处罚内容
         */
        private String Content;
        /**
         * 作出行政处罚决定日期
         */
        private String PenaltyDate;
        /**
         * 作出行政公示日期
         */
        private String PublicDate;
    }
}
