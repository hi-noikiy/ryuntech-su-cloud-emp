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
    private String employeeId;
    private String monitorId;
    /**
     * 0已取消 1监控中
     */
    private String status;
    /**
     * 是否发送微信消息推送0否1是
     */
    private Boolean isWeChat;
    /**
     * 是否发送邮箱消息推送0否1是
     */
    private Boolean isEmall;
    private List<String> customerIds;
    /**
     * 客户编号集合
     */

}
