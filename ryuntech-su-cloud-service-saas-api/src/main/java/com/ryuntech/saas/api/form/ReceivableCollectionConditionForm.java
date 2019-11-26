package com.ryuntech.saas.api.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 回款管理接收前台条件参数
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class ReceivableCollectionConditionForm extends BaseModel {

    /**
     * 回款方式
     */
    private String type;

    /**
     * 客户名
     */
    private String customerName;

    /**
     * 回款开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 回款结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
