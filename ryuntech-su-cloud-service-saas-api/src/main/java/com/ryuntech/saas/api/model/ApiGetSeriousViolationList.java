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
public class ApiGetSeriousViolationList extends BaseApi {
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        /**
         * 类型
         */
        private String Type;
        /**
         * 列入原因
         */
        private String AddReason;
        /**
         * 列入时间
         */
        private String AddDate;
        /**
         * 列入决定机关
         */
        private String AddOffice;
        /**
         * 移除原因
         */
        private String RemoveReason;
        /**
         * 移除时间
         */
        private String RemoveDate;
        /**
         * 移除决定机关
         */
        private String RemoveOffice;
    }
}
