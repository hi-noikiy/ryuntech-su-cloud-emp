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
public class ApiGetEnvPunishmentList {

    private ApiHeader apiHeader;
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String Id;
        /**
         * 决定书文号
         */
        private String CaseNo;
        /**
         * 处罚日期
         */
        private String PunishDate;
        /**
         * 违法类型
         */
        private String IllegalType;
        /**
         * 处罚单位
         */
        private String PunishGov;
    }
}
