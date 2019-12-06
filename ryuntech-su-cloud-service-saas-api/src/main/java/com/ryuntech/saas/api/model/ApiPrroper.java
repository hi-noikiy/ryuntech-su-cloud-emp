package com.ryuntech.saas.api.model;

import com.ryuntech.common.model.BaseApi;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author EDZ
 */

/**
 * 个人关联风险-法人变更
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApiPrroper extends BaseApi {



    private List<Result> results;


    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String CompanyName;
        private String KeyNo;
        private String RoleDesc;
        private String Count;
        private List<Oper> OperList;
    }
    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Oper{
        private List<BeforeOper> beforeOpers;
        private List<AfterOper>  afterOpers;
        private String  ChangeDate;
    }
    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class BeforeOper{
        private String Org;
        private String KeyNo;
        private String Name;
    }
    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class AfterOper{
        private String Org;
        private String KeyNo;
        private String Name;
    }
}
