package com.ryuntech.saas.api.service;

import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.form.ReceiveBalanceForm;
import com.ryuntech.saas.api.form.RepayPlanForm;

public interface IReportService {
    Result receiveBalanceByDepartmentList(ReceiveBalanceForm form) throws Exception;

    Result receiveBalanceByEmployeePager(ReceiveBalanceForm receiveBalanceForm) throws Exception;

    Result receiveBalanceByCustomerList(ReceiveBalanceForm receiveBalanceForm) throws Exception;

    Result repayPlanByContractPager(RepayPlanForm repayPlanForm) throws Exception;

    Result repayPlanByContractTotal(RepayPlanForm repayPlanForm) throws Exception;

    Result repayPlanByDepartmentPager(RepayPlanForm repayPlanForm) throws Exception;

    Result repayPlanByDepartmentTotal(RepayPlanForm repayPlanForm) throws Exception;

    Result repayPlanByCustomerPager(RepayPlanForm repayPlanForm) throws Exception;

    Result repayPlanByCustomerDetailPager(RepayPlanForm repayPlanForm) throws Exception;

    Result repayPlanByCustomerTotal(RepayPlanForm repayPlanForm) throws Exception;

    Result repayPlanByEmployeePager(RepayPlanForm repayPlanForm) throws Exception;

    Result repayPlanByEmployeeTotal(RepayPlanForm repayPlanForm) throws Exception;

    Result repayPlanByEmployeeDetailPager(RepayPlanForm repayPlanForm) throws Exception;

    Result overdueByContractPager(RepayPlanForm repayPlanForm) throws Exception;

    Result overdueByContractTotal(String departmentId) throws Exception;

    Result overdueByCustomerPager(RepayPlanForm repayPlanForm) throws Exception;

    Result overdueByCustomerRank(String departmentId) throws Exception;

    Result overdueByEmployeePager(RepayPlanForm repayPlanForm) throws Exception;

    Result overdueByEmployeeRank(String departmentId) throws Exception;
}
