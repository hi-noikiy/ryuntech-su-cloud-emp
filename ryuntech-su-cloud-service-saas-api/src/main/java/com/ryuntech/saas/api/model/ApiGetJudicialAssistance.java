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
public class ApiGetJudicialAssistance {

    private ApiHeader apiHeader;
    private List<Result> results;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Result{
        private String ExecutedBy;
        private String EquityAmount;
        private String EnforcementCourt;
        private String ExecutionNoticeNum;
        private String Status;
        private EquityFreezeDetail EquityFreezeDetail;
        private EquityUnFreezeDetail equityUnFreezeDetail;
        private String ExecutionMatters;
        private String ExecutionVerdictNum;
        private String ExecutionDocNum;
        private String ExecutedPersonDocType;
        private String ExecutedPersonDocNum;
        private String UnFreezeDate;
        private String PublicDate;
        private String ThawOrgan;
        private String ThawDocNo;
        private JudicialPartnersChangeDetail JudicialPartnersChangeDetail;

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class EquityUnFreezeDetail{
            private String ExecutionMatters;
            private String ExecutionVerdictNum;
            private String ExecutionDocNum;
            private String ExecutedPersonDocType;
            private String ExecutedPersonDocNum;
            private String UnFreezeDate;
            private String PublicDate;
            private String ThawOrgan;
            private String ThawDocNo;
        }

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class JudicialPartnersChangeDetail{
            private String ExecutionMatters;
            private String ExecutionVerdictNum;
            private String ExecutedPersonDocType;
            private String ExecutedPersonDocNum;
            private String Assignee;
            private String AssistExecDate;
            private String AssigneeDocKind;
            private String AssigneeRegNo;
            private String StockCompanyName;
        }

        @Data
        @EqualsAndHashCode(callSuper = false)
        @Accessors(chain = true)
        public class EquityFreezeDetail{
            private String CompanyName;
            private String ExecutionMatters;
            private String ExecutionDocNum;
            private String ExecutionVerdictNum;
            private String ExecutedPersonDocType;
            private String ExecutedPersonDocNum;
            private String FreezeStartDate;
            private String FreezeEndDate;
            private String FreezeTerm;
            private String PublicDate;
        }
    }
}
