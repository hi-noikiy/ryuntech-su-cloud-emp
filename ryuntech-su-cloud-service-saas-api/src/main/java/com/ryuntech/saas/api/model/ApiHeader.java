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
public class ApiHeader {

    private String Status;
    private String Message;
    private String OrderNumber;

    private Paging paging;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    class Paging{
        private String PageSize;
        private String PageIndex;
        private String TotalRecords;
    }


}
