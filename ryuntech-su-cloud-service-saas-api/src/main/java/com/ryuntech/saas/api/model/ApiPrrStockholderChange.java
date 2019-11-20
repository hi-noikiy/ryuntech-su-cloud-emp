package com.ryuntech.saas.api.model;

import com.ryuntech.common.model.BaseModel;
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
public class ApiPrrStockholderChange extends BaseModel {

    private ApiHeader apiHeader;

    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String CompanyName;
        private String KeyNo;
        private String RoleDesc;
        private String Count;
        private List<Partner> PartnerList;
    }
    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Partner{
        private List<OldPartners> oldPartners;
        private List<NewPartners> newPartners;
        private String ChangeDate;

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class OldPartners{
            private String Org;
            private String KeyNo;
            private String Name;
            private String StockPercent;
        }
        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class NewPartners{
            private String Org;
            private String KeyNo;
            private String Name;
            private String StockPercent;
        }
    }
}
