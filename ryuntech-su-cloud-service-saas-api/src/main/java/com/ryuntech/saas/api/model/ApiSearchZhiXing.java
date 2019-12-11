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
public class ApiSearchZhiXing extends BaseApi {
    private List<Result> Result;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String Id;
        private String Sourceid;
        private String Name;
        private String Liandate;
        private String Anno;
        private String ExecuteGov;
        private String Biaodi;
        private String Status;
        private String PartyCardNum;
        private String Updatedate;
    }
}
