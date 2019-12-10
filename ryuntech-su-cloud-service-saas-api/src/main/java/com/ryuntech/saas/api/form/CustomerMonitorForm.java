package com.ryuntech.saas.api.form;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class CustomerMonitorForm {


    private String customerId;
    private String monitorId;
    private List<String> customerIds;
    /**
     * 客户编号集合
     */

}
