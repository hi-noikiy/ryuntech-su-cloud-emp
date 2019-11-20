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
public class ApiSearchShiXin {
    private ApiHeader apiHeader;
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String Id;
        private String Sourceid;
        private String Uniqueno;
        private String Name;
        private String Liandate;
        private String Anno;
        private String Orgno;
        private String Ownername;
        private String Executegov;
        private String Province;
        private String Executeunite;
        private String Yiwu;
        private String Executestatus;
        private String Actionremark;
        private String Publicdate;
        private String Age;
        private String Sexy;
        private String Updatedate;
        private String Executeno;
        private String Performedpart;
        private String Unperformpart;
        private String OrgType;
        private String OrgTypeName;
    }
}
