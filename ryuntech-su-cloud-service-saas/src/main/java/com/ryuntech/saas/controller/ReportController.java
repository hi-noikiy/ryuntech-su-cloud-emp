package com.ryuntech.saas.controller;

import com.ryuntech.common.constant.Global;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.form.ReceiveBalanceForm;
import com.ryuntech.saas.api.form.RepayPlanForm;
import com.ryuntech.saas.api.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private IReportService reportService;

    // region 应收统计报表

    // 按部门
    @RequestMapping("receiveBalance/byDepartment/list")
    public Result receiveBalanceByDepartmentList(
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime) {
        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        try {
            return reportService.receiveBalanceByDepartmentList(new ReceiveBalanceForm(departmentId, beginTime, endTime));
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按员工
    @RequestMapping("receiveBalance/byEmployee/list")
    public Result receiveBalanceByEmployeeList(
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime) {
        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        try {
            return reportService.receiveBalanceByEmployeePager(new ReceiveBalanceForm(departmentId, beginTime, endTime));
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按客户
    @RequestMapping("receiveBalance/byCustomer/list")
    public Result receiveBalanceByCustomerList(
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime) {
        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        try {
            return reportService.receiveBalanceByCustomerList(new ReceiveBalanceForm(departmentId, beginTime, endTime));
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // endregion

    // region 回款计划报表

    // 按合同分页
    @RequestMapping("repaymentPlan/byContract/pager")
    public Result repaymentPlanByContractPager(
            @RequestParam("status") String status,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime,
            @RequestParam(value = "pageCode", required = false) String pageCode,
            @RequestParam(value = "pageSize", required = false) String pageSize) {

        // 回款状态(0逾期1已还款2未开始3回款中)
        if (!StringUtil.isEmpty(status)) {
            if (!StringUtil.contains(false, status, "0", "1", "2", "3")) {
                return new Result(CommonEnums.PARAM_ERROR, "回款计划状态不正确");
            }
        } else {
            status = null;
        }

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        if (!StringUtil.isNumber(pageCode)) {
            pageCode = Global.PAGE_CODE;
        }

        if (!StringUtil.isNumber(pageSize)) {
            pageSize = Global.PAGE_SIZE;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        RepayPlanForm repayPlanForm = new RepayPlanForm(status, beginTime, endTime, Integer.parseInt(pageCode), Integer.parseInt(pageSize));
        repayPlanForm.setDepartmentId(departmentId);

        try {
            return reportService.repayPlanByContractPager(repayPlanForm);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按合同求总
    @RequestMapping("repaymentPlan/byContract/total")
    public Result repaymentPlanByContractTotal(
            @RequestParam("status") String status,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime) {

        // 回款状态(0逾期1已还款2未开始3回款中)
        if (!StringUtil.isEmpty(status)) {
            if (!StringUtil.contains(false, status, "0", "1", "2", "3")) {
                return new Result(CommonEnums.PARAM_ERROR, "回款计划状态不正确");
            }
        } else {
            status = null;
        }

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        try {
            return reportService.repayPlanByContractTotal(new RepayPlanForm(status, departmentId, beginTime, endTime));
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按部门分页
    @RequestMapping("repaymentPlan/byDepartment/pager")
    public Result repaymentPlanByDepartmentPager(
            @RequestParam("status") String status,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime,
            @RequestParam(value = "pageCode", required = false) String pageCode,
            @RequestParam(value = "pageSize", required = false) String pageSize) {

        // 回款状态(0逾期1已还款2未开始3回款中)
        if (!StringUtil.isEmpty(status)) {
            if (!StringUtil.contains(false, status, "0", "1", "2", "3")) {
                return new Result(CommonEnums.PARAM_ERROR, "回款计划状态不正确");
            }
        } else {
            status = null;
        }

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        if (!StringUtil.isNumber(pageCode)) {
            pageCode = Global.PAGE_CODE;
        }

        if (!StringUtil.isNumber(pageSize)) {
            pageSize = Global.PAGE_SIZE;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        RepayPlanForm repayPlanForm = new RepayPlanForm(status, beginTime, endTime, Integer.parseInt(pageCode), Integer.parseInt(pageSize));
        repayPlanForm.setDepartmentId(departmentId);

        try {
            return reportService.repayPlanByDepartmentPager(repayPlanForm);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按部门求总
    @RequestMapping("repaymentPlan/byDepartment/total")
    public Result repaymentPlanByDepartmentTotal(
            @RequestParam("status") String status,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime) {

        // 回款状态(0逾期1已还款2未开始3回款中)
        if (!StringUtil.isEmpty(status)) {
            if (!StringUtil.contains(false, status, "0", "1", "2", "3")) {
                return new Result(CommonEnums.PARAM_ERROR, "回款计划状态不正确");
            }
        } else {
            status = null;
        }

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        try {
            return reportService.repayPlanByDepartmentTotal(new RepayPlanForm(status, departmentId, beginTime, endTime));
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按客户分页
    @RequestMapping("repaymentPlan/byCustomer/pager")
    public Result repaymentPlanByCustomerPager(
            @RequestParam("status") String status,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime,
            @RequestParam(value = "pageCode", required = false) String pageCode,
            @RequestParam(value = "pageSize", required = false) String pageSize) {

        // 回款状态(0逾期1已还款2未开始3回款中)
        if (!StringUtil.isEmpty(status)) {
            if (!StringUtil.contains(false, status, "0", "1", "2", "3")) {
                return new Result(CommonEnums.PARAM_ERROR, "回款计划状态不正确");
            }
        } else {
            status = null;
        }

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        if (!StringUtil.isNumber(pageCode)) {
            pageCode = Global.PAGE_CODE;
        }

        if (!StringUtil.isNumber(pageSize)) {
            pageSize = Global.PAGE_SIZE;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        RepayPlanForm repayPlanForm = new RepayPlanForm(status, beginTime, endTime, Integer.parseInt(pageCode), Integer.parseInt(pageSize));
        repayPlanForm.setDepartmentId(departmentId);

        try {
            return reportService.repayPlanByCustomerPager(repayPlanForm);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按客户求总
    @RequestMapping("repaymentPlan/byCustomer/total")
    public Result repaymentPlanByCustomerTotal(
            @RequestParam("status") String status,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime) {

        // 回款状态(0逾期1已还款2未开始3回款中)
        if (!StringUtil.isEmpty(status)) {
            if (!StringUtil.contains(false, status, "0", "1", "2", "3")) {
                return new Result(CommonEnums.PARAM_ERROR, "回款计划状态不正确");
            }
        } else {
            status = null;
        }

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        try {
            return reportService.repayPlanByCustomerTotal(new RepayPlanForm(status, departmentId, beginTime, endTime));
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按客户详细
    @RequestMapping("repaymentPlan/byCustomer/detail")
    public Result repaymentPlanByCustomerDetail(
            @RequestParam("status") String status,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("customerId") String customerId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime,
            @RequestParam(value = "pageCode", required = false) String pageCode,
            @RequestParam(value = "pageSize", required = false) String pageSize) {

        // 回款状态(0逾期1已还款2未开始3回款中)
        if (!StringUtil.isEmpty(status)) {
            if (!StringUtil.contains(false, status, "0", "1", "2", "3")) {
                return new Result(CommonEnums.PARAM_ERROR, "回款计划状态不正确");
            }
        } else {
            status = null;
        }

        if (StringUtil.isEmpty(customerId)) {
            return new Result(CommonEnums.PARAM_ERROR, "客户ID不正确");
        }

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        if (!StringUtil.isNumber(pageCode)) {
            pageCode = Global.PAGE_CODE;
        }

        if (!StringUtil.isNumber(pageSize)) {
            pageSize = Global.PAGE_SIZE;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        RepayPlanForm repayPlanForm = new RepayPlanForm(status, beginTime, endTime, Integer.parseInt(pageCode), Integer.parseInt(pageSize));
        repayPlanForm.setDepartmentId(departmentId);
        repayPlanForm.setCustomerId(customerId);

        try {
            return reportService.repayPlanByCustomerDetailPager(repayPlanForm);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按员工分页
    @RequestMapping("repaymentPlan/byEmployee/pager")
    public Result repaymentPlanByEmployeePager(
            @RequestParam("status") String status,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime,
            @RequestParam(value = "pageCode", required = false) String pageCode,
            @RequestParam(value = "pageSize", required = false) String pageSize) {

        // 回款状态(0逾期1已还款2未开始3回款中)
        if (!StringUtil.isEmpty(status)) {
            if (!StringUtil.contains(false, status, "0", "1", "2", "3")) {
                return new Result(CommonEnums.PARAM_ERROR, "回款计划状态不正确");
            }
        } else {
            status = null;
        }

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        if (!StringUtil.isNumber(pageCode)) {
            pageCode = Global.PAGE_CODE;
        }

        if (!StringUtil.isNumber(pageSize)) {
            pageSize = Global.PAGE_SIZE;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        RepayPlanForm repayPlanForm = new RepayPlanForm(status, beginTime, endTime, Integer.parseInt(pageCode), Integer.parseInt(pageSize));
        repayPlanForm.setDepartmentId(departmentId);

        try {
            return reportService.repayPlanByEmployeePager(repayPlanForm);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按员工求总
    @RequestMapping("repaymentPlan/byEmployee/total")
    public Result repaymentPlanByEmployeeTotal(
            @RequestParam("status") String status,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime) {

        // 回款状态(0逾期1已还款2未开始3回款中)
        if (!StringUtil.isEmpty(status)) {
            if (!StringUtil.contains(false, status, "0", "1", "2", "3")) {
                return new Result(CommonEnums.PARAM_ERROR, "回款计划状态不正确");
            }
        } else {
            status = null;
        }

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        try {
            return reportService.repayPlanByEmployeeTotal(new RepayPlanForm(status, departmentId, beginTime, endTime));
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按员工详细
    @RequestMapping("repaymentPlan/byEmployee/detail")
    public Result repaymentPlanByEmployeeDetail(
            @RequestParam("employeeId") String employeeId,
            @RequestParam("status") String status,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime,
            @RequestParam(value = "pageCode", required = false) String pageCode,
            @RequestParam(value = "pageSize", required = false) String pageSize) {

        // 回款状态(0逾期1已还款2未开始3回款中)
        if (!StringUtil.isEmpty(status)) {
            if (!StringUtil.contains(false, status, "0", "1", "2", "3")) {
                return new Result(CommonEnums.PARAM_ERROR, "回款计划状态不正确");
            }
        } else {
            status = null;
        }

        if (StringUtil.isEmpty(employeeId)) {
            return new Result(CommonEnums.PARAM_ERROR, "员工ID不正确");
        }

        if (!StringUtil.isNumber(pageCode)) {
            pageCode = Global.PAGE_CODE;
        }

        if (!StringUtil.isNumber(pageSize)) {
            pageSize = Global.PAGE_SIZE;
        }

        try {
            checkTime(beginTime, endTime);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }

        RepayPlanForm repayPlanForm = new RepayPlanForm(status, beginTime, endTime, Integer.parseInt(pageCode), Integer.parseInt(pageSize));
        repayPlanForm.setEmployeeId(employeeId);
        try {
            return reportService.repayPlanByEmployeeDetailPager(repayPlanForm);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // endregion

    // region 逾期统计报表

    @RequestMapping("overdue/byContract/pager")
    public Result overdueByContractPager(
            @RequestParam("departmentId") String departmentId,
            @RequestParam(value = "pageCode", required = false) String pageCode,
            @RequestParam(value = "pageSize", required = false) String pageSize) {

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        if (!StringUtil.isNumber(pageCode)) {
            pageCode = Global.PAGE_CODE;
        }

        if (!StringUtil.isNumber(pageSize)) {
            pageSize = Global.PAGE_SIZE;
        }

        RepayPlanForm repayPlanForm = new RepayPlanForm(departmentId, Integer.parseInt(pageCode), Integer.parseInt(pageSize));

        try {
            return reportService.overdueByContractPager(repayPlanForm);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // 按合同求总
    @RequestMapping("overdue/byContract/total")
    public Result overdueByContractTotal(
            @RequestParam("departmentId") String departmentId) {
        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        try {
            return reportService.overdueByContractTotal(departmentId);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    @RequestMapping("overdue/byCustomer/pager")
    public Result overdueByCustomerPager(
            @RequestParam("departmentId") String departmentId,
            @RequestParam(value = "pageCode", required = false) String pageCode,
            @RequestParam(value = "pageSize", required = false) String pageSize) {

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        if (!StringUtil.isNumber(pageCode)) {
            pageCode = Global.PAGE_CODE;
        }

        if (!StringUtil.isNumber(pageSize)) {
            pageSize = Global.PAGE_SIZE;
        }

        RepayPlanForm repayPlanForm = new RepayPlanForm(departmentId, Integer.parseInt(pageCode), Integer.parseInt(pageSize));

        try {
            return reportService.overdueByCustomerPager(repayPlanForm);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    @RequestMapping("overdue/byCustomer/rank")
    public Result overdueByCustomerRank(
            @RequestParam("departmentId") String departmentId) {
        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        try {
            return reportService.overdueByCustomerRank(departmentId);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    @RequestMapping("overdue/byEmployee/pager")
    public Result overdueByEmployeePager(
            @RequestParam("departmentId") String departmentId,
            @RequestParam(value = "pageCode", required = false) String pageCode,
            @RequestParam(value = "pageSize", required = false) String pageSize) {

        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        if (!StringUtil.isNumber(pageCode)) {
            pageCode = Global.PAGE_CODE;
        }

        if (!StringUtil.isNumber(pageSize)) {
            pageSize = Global.PAGE_SIZE;
        }

        RepayPlanForm repayPlanForm = new RepayPlanForm(departmentId, Integer.parseInt(pageCode), Integer.parseInt(pageSize));

        try {
            return reportService.overdueByEmployeePager(repayPlanForm);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    @RequestMapping("overdue/byEmployee/rank")
    public Result overdueByEmployeeRank(
            @RequestParam("departmentId") String departmentId) {
        if (StringUtil.isEmpty(departmentId)) {
            departmentId = null;
        }

        try {
            return reportService.overdueByEmployeeRank(departmentId);
        } catch (Exception e) {
            return new Result(CommonEnums.PARAM_ERROR, e.getLocalizedMessage());
        }
    }

    // endregion

    // 校验时间，默认进来，开始时间=当月第一天，结束时间=当月最后一天
    private void checkTime(String beginTime, String endTime) throws Exception {
        Calendar calendar = Calendar.getInstance();
        // 默认取当月第1天
        if (StringUtil.isEmpty(beginTime)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            beginTime = DateUtil.formatDate(calendar.getTime());
        } else {
            Date dTime = DateUtil.parse(beginTime, "yyyy-MM-dd");
            if (dTime == null) {
                throw new Exception("开始时间不正确");
            }
        }

        // 默认取当月最后1天
        if (StringUtil.isEmpty(endTime)) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            endTime = DateUtil.formatDate(calendar.getTime());
        } else {
            Date dTime = DateUtil.parse(endTime, "yyyy-MM-dd");
            if (dTime == null) {
                throw new Exception("结束时间不正确");
            }
        }

        if (DateUtil.after(beginTime, endTime, "yyyy-MM-dd")) {
            throw new Exception("开始时间不能超过结束时间");
        }
    }
}
