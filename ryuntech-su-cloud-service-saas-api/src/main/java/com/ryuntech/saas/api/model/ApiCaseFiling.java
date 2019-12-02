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
public class ApiCaseFiling extends BaseApi {

    private ApiHeader apiHeader;
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String Id;
        private String CaseNo;
        private String PublishDate;
        private String CourtYear;
        private List<Prosecutor> ProsecutorList;
        private List<Defendant> DefendantList;

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class Prosecutor{
            private String Name;
            private String KeyNo;
        }

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class Defendant{
            private String Name;
            private String KeyNo;
        }
    }
}

