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
public class ApiSearchCourtAnnouncement {
    private ApiHeader apiHeader;
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String UploadDate;
        private String Court;
        private String Content;
        private String Category;
        private String PublishedDate;
        private String PublishedPage;
        private String Party;
        private String Id;
        private List<Prosecutor> ProsecutorList;
        private List<Defendant> DefendantList;
        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class Prosecutor{
            private String KeyNo;
            private String Name;
        }

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class Defendant{
            private String KeyNo;
            private String Name;
        }
    }
}
