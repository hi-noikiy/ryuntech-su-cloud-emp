package com.ryuntech.saas.api.model;


import com.ryuntech.common.model.BaseApi;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApiGetBasicDetailsByName extends BaseApi {

    private Result Result;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String KeyNo;
        private String Name;
        private String No;
        private String BelongOrg;
        private String OperName;
        private String StartDate;
        private String EndDate;
        private String Status;
        private String Province;
        private String UpdatedDate;
        private String CreditCode;
        private String RegistCapi;
        private String EconKind;
        private String Address;
        private String Scope;
        private String TermStart;
        private String TeamEnd;
        private String CheckDate;
        private String OrgNo;
        private String IsOnStock;
        private String StockNumber;
        private String StockType;
        private String EntType;
        private String RecCap;

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class OriginalName{
            private String Name;
            private String ChangeDate;
        }
    }
}
