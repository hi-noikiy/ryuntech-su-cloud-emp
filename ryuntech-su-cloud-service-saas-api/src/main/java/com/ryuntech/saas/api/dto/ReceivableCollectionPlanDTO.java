package com.ryuntech.saas.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class ReceivableCollectionPlanDTO extends BaseModel {

    /*
    * 计划金额
    * */
    private String planAmount;
    /**
     * 计划编号
     */
    private String planId;
    /**
     * 合同编号
     */
    private String contractId;
    /**
     * 已回款金额
     */
    private String backAmount;

    /**
     * 备注
     */
    private String remakes;

    /**
     * 计划回款时间
     */
    private String planTime;
    /**
     * 回款状态(0逾期1已还款2未开始)
     */
    private String status;
}
