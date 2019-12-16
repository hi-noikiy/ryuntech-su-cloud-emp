package com.ryuntech.saas.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RepayPlanForm {
    private String planStatus;

    private String customerId;

    private String employeeId;

    private String departmentId;

    private String beginTime;

    private String endTime;

    private Integer pageCode;

    private Integer pageSize;

    public RepayPlanForm(String planStatus, String departmentId, String beginTime, String endTime, Integer pageCode, Integer pageSize) {
        this.planStatus = planStatus;
        this.departmentId = departmentId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.pageCode = pageCode;
        this.pageSize = pageSize;
    }

    public RepayPlanForm(String customerId, String planStatus, String departmentId, String beginTime, String endTime, Integer pageCode, Integer pageSize) {
        this.customerId = customerId;
        this.planStatus = planStatus;
        this.departmentId = departmentId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.pageCode = pageCode;
        this.pageSize = pageSize;
    }

    public RepayPlanForm(String planStatus, String departmentId, String beginTime, String endTime) {
        this.planStatus = planStatus;
        this.departmentId = departmentId;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public RepayPlanForm(String planStatus, String beginTime, String endTime, Integer pageCode, Integer pageSize) {
        this.planStatus = planStatus;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.pageCode = pageCode;
        this.pageSize = pageSize;
    }

    public RepayPlanForm(String departmentId, Integer pageCode, Integer pageSize) {
        this.departmentId = departmentId;
        this.pageCode = pageCode;
        this.pageSize = pageSize;
    }
}
