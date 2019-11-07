package com.ryuntech.saas.api.form;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


/**
 * 合同计划接受前台参数
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class ReceivableCollectionPlanForm extends BaseModel {

    /**
     * 主键
     */
    private String planId;

    /**
     * 计划金额
     */
    private String planAmount;

    /**
     * 备注
     */
    private String remakes;

    /**
     * 回款状态(0逾期1已还款2未开始3回款中)
     */
    private String status;
    /**
     * 状态集
     */
    private List<String> statusList;

    /**
     * 合同编号
     */
    private String contractId;

    /**
     * 计划回款时间
     */
    private Date planTime;
}
