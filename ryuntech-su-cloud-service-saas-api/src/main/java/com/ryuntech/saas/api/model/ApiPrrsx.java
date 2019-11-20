package com.ryuntech.saas.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 个人关联风险-失信被执行
 * @author EDZ
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApiPrrsx {

    private ApiHeader apiHeader;

    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    class Result{
        private String CompanyName;
        private String KeyNo;
        private String RoleDesc;
        private String Count;
    }
}
