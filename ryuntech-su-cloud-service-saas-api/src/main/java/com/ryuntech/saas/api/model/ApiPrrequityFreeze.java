package com.ryuntech.saas.api.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 股权冻结
 * @author EDZ
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApiPrrequityFreeze {
    private ApiHeader apiHeader;
    private List<Result> results;


    public class Result{
        private String ExecutedBy;
        private String EquityAmount;
        private String EnforcementCourt;
        private String ExecutionNoticeNum;
        private String Status;
        private String EquityFreezeDetail;
        private EquityUnFreezeDetail equityUnFreezeDetail;
        private String JudicialPartnersChangeDetail;
    }
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
}
