package com.ryuntech.saas.api.dto;

import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;


/**
 *
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class WeChatIndexDTO extends BaseModel {
    private String token;
    /**
     * 员工编号
     */
    private String employeeId;

    /**
     * 计划状态
     */
    private String status;
    private List<String> statusList;
    /**
     * 合同状态
     */
    private String contractStatus;
    /**
     * 详情类型
     */
    private String type;

    /**
     * 合同开始时间
     */
    private String startDate;

    /**
     * 合同结束时间
     */
    private String endDate;

    /**
     * 计划开始时间
     */
    private String planStartDate;

    /**
     * 计划结束时间
     */
    private String planEndDate;


}
