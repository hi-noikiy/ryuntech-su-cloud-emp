package com.ryuntech.saas.api.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/** 首页数据简报请求参数
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class IndexDataBriefingForm extends BaseModel {

    /**
     * 部门集合
     */
    private List<String> departmentNames;

    /**
     * 查询月份
     */
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date sameMonth;

    /**
     * 部门内合同编号集合
     */
    private List<String> contractIds;
}
