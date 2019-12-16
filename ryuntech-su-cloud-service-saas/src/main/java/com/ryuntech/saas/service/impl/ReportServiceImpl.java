package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.dto.*;
import com.ryuntech.saas.api.form.ReceiveBalanceForm;
import com.ryuntech.saas.api.form.RepayPlanForm;
import com.ryuntech.saas.api.mapper.*;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.Department;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.service.IDepartmentService;
import com.ryuntech.saas.api.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    ReceivableContractMapper receivableContractMapper;

    @Autowired
    CustomerUserInfoMapper customerUserInfoMapper;

    @Autowired
    ReceivableCollectionPlanMapper receivableCollectionPlanMapper;

    // region 应收统计
    public Result receiveBalanceByDepartmentList(ReceiveBalanceForm form) throws Exception {
        List<String> departmentIds = getCurrentUserDepartments(form.getDepartmentId());

        List<ReceiveBalanceByDepartmentDTO> rs = new ArrayList<>();
        for (String departmentId : departmentIds) {
            rs.add(getReceiveBalanceByDepartment(departmentId, form));
        }
        return new Result(rs);
    }

    // 指定时间内新增的合同总金额 不匹配 指定时间内部门的所有回款（可能不包含前面这些合同）所以要分开查询
    private ReceiveBalanceByDepartmentDTO getReceiveBalanceByDepartment(String rootDepartmentId, ReceiveBalanceForm form) {
        List<String> allDepartmentIds = new ArrayList<>();
        allDepartmentIds.add(rootDepartmentId);

        List<String> departmentIds = departmentService.getChilrensDepartments(rootDepartmentId);
        allDepartmentIds.addAll(departmentIds);

        Department department = departmentMapper.selectById(rootDepartmentId);

        ReceiveBalanceByDepartmentDTO receiveBalanceByDepartmentDTO = new ReceiveBalanceByDepartmentDTO();
        receiveBalanceByDepartmentDTO.setDepartmentName(department.getDepartmentName());
        // 部门该时间段 新增合同金额总和
        String saleAmount = receivableContractMapper.getDepartmentsContractAmounts(allDepartmentIds, form.getBeginTime(), form.getEndTime());
        if (StringUtil.isEmpty(saleAmount)) {
            receiveBalanceByDepartmentDTO.setSaleAmount(new BigDecimal("0.00"));
        } else {
            receiveBalanceByDepartmentDTO.setSaleAmount(new BigDecimal(saleAmount).setScale(2, RoundingMode.HALF_UP));
        }

        // 部门该时间段 新增回款金额总和
        String repayAmount = receivableContractMapper.getDepartmentsRepayAmounts(allDepartmentIds, form.getBeginTime(), form.getEndTime());
        if (StringUtil.isEmpty(repayAmount)) {
            receiveBalanceByDepartmentDTO.setRepayAmount(new BigDecimal("0.00"));
        } else {
            receiveBalanceByDepartmentDTO.setRepayAmount(new BigDecimal(repayAmount).setScale(2, RoundingMode.HALF_UP));
        }

        // 截止到endTine 该部门总销售额 -该部门总回款额
        BigDecimal receiveBalance = new BigDecimal("0.00");
        BigDecimal bigSaleAmount = new BigDecimal("0.00");
        BigDecimal bigRepayAmount = new BigDecimal("0.00");

        saleAmount = receivableContractMapper.getDepartmentsContractAmounts(allDepartmentIds, null, form.getEndTime());
        if (!StringUtil.isEmpty(saleAmount)) {
            bigSaleAmount = new BigDecimal(saleAmount).setScale(2, RoundingMode.HALF_UP);
            repayAmount = receivableContractMapper.getDepartmentsRepayAmounts(allDepartmentIds, null, form.getEndTime());
            if (!StringUtil.isEmpty(repayAmount)) {
                bigRepayAmount = new BigDecimal(repayAmount).setScale(2, RoundingMode.HALF_UP);
            }
            receiveBalance = bigSaleAmount.subtract(bigRepayAmount);
        }
        receiveBalanceByDepartmentDTO.setReceiveBalance(receiveBalance);

        return receiveBalanceByDepartmentDTO;
    }

    @Override
    public Result receiveBalanceByEmployeePager(ReceiveBalanceForm form) throws Exception {
        List<String> departmentIds = getCurrentUserDepartments(form.getDepartmentId());

        List<ReceiveBalanceByEmployeeDTO> dtoList = new ArrayList<>();
        List<ReceiveBalanceContractAmount> list1 = receivableContractMapper.getEmployeesContractAmounts(departmentIds, form.getBeginTime(), form.getEndTime());
        Map<String, String> m1 = list1.stream().collect(Collectors.toMap(ReceiveBalanceContractAmount::getEmployeeId, ReceiveBalanceContractAmount::getSaleAmount));

        List<ReceiveBalanceContractAmount> list2 = receivableContractMapper.getEmployeesContractAmounts(departmentIds, null, form.getEndTime());
        Map<String, String> m2 = list2.stream().collect(Collectors.toMap(ReceiveBalanceContractAmount::getEmployeeId, ReceiveBalanceContractAmount::getSaleAmount));

        List<ReceiveBalanceRepayAmount> list3 = receivableContractMapper.getEmployeesRepayAmounts(departmentIds, form.getBeginTime(), form.getEndTime());
        Map<String, String> m3 = list3.stream().collect(Collectors.toMap(ReceiveBalanceRepayAmount::getEmployeeId, ReceiveBalanceRepayAmount::getRepayAmount));

        List<ReceiveBalanceRepayAmount> list4 = receivableContractMapper.getEmployeesRepayAmounts(departmentIds, null, form.getEndTime());
        Map<String, String> m4 = list4.stream().collect(Collectors.toMap(ReceiveBalanceRepayAmount::getEmployeeId, ReceiveBalanceRepayAmount::getRepayAmount));

        for (String employeeId : m1.keySet()) {
            Employee employee = employeeMapper.selectById(employeeId);
            Department department = departmentMapper.selectById(employee.getDepartmentId());

            ReceiveBalanceByEmployeeDTO dto = new ReceiveBalanceByEmployeeDTO();
            dto.setDepartmentName(department.getDepartmentName());
            dto.setEmployeeName(employee.getName());

            BigDecimal saleAmount = new BigDecimal("0.00");
            BigDecimal receiveBalance = new BigDecimal("0.00");
            BigDecimal repayAmount = new BigDecimal("0.00");

            String strSaleAmount = m1.get(employeeId);
            if (!StringUtil.isEmpty(strSaleAmount)) {
                saleAmount = new BigDecimal(strSaleAmount).setScale(2, RoundingMode.HALF_UP);
            }

            String strRepayAmount = m3.get(employeeId);
            if (!StringUtil.isEmpty(strRepayAmount)) {
                repayAmount = new BigDecimal(strRepayAmount).setScale(2, RoundingMode.HALF_UP);
            }

            String strAllSaleAmount = m2.get(employeeId);
            if (!StringUtil.isEmpty(strAllSaleAmount)) {
                BigDecimal saleAmountTmp = new BigDecimal(strAllSaleAmount).setScale(2, RoundingMode.HALF_UP);
                BigDecimal repayAmountTmp = new BigDecimal("0.00");
                String strAllRepayAmount = m4.get(employeeId);
                if (!StringUtil.isEmpty(strAllRepayAmount)) {
                    repayAmountTmp = new BigDecimal(strAllRepayAmount).setScale(2, RoundingMode.HALF_UP);
                }
                receiveBalance = saleAmountTmp.subtract(repayAmountTmp);
            }

            dto.setReceiveBalance(receiveBalance);
            dto.setRepayAmount(repayAmount);
            dto.setSaleAmount(saleAmount);
            dtoList.add(dto);
        }
        return new Result(dtoList);
    }

    @Override
    public Result receiveBalanceByCustomerList(ReceiveBalanceForm form) throws Exception {
        List<String> departmentIds = getCurrentUserDepartments(form.getDepartmentId());

        List<ReceiveBalanceByCustomerDTO> dtoList = new ArrayList<>();
        List<ReceiveBalanceContractAmount> list1 = receivableContractMapper.getCustomersContractAmounts(departmentIds, form.getBeginTime(), form.getEndTime());
        Map<String, String> m1 = list1.stream().collect(Collectors.toMap(ReceiveBalanceContractAmount::getCustomerId, ReceiveBalanceContractAmount::getSaleAmount));

        List<ReceiveBalanceContractAmount> list2 = receivableContractMapper.getCustomersContractAmounts(departmentIds, null, form.getEndTime());
        Map<String, String> m2 = list2.stream().collect(Collectors.toMap(ReceiveBalanceContractAmount::getCustomerId, ReceiveBalanceContractAmount::getSaleAmount));

        List<ReceiveBalanceRepayAmount> list3 = receivableContractMapper.getCustomersRepayAmounts(departmentIds, form.getBeginTime(), form.getEndTime());
        Map<String, String> m3 = list3.stream().collect(Collectors.toMap(ReceiveBalanceRepayAmount::getCustomerId, ReceiveBalanceRepayAmount::getRepayAmount));

        List<ReceiveBalanceRepayAmount> list4 = receivableContractMapper.getCustomersRepayAmounts(departmentIds, null, form.getEndTime());
        Map<String, String> m4 = list4.stream().collect(Collectors.toMap(ReceiveBalanceRepayAmount::getCustomerId, ReceiveBalanceRepayAmount::getRepayAmount));

        for (String customerId : m1.keySet()) {
            CustomerUserInfo customerUserInfo = customerUserInfoMapper.selectById(customerId);

            ReceiveBalanceByCustomerDTO dto = new ReceiveBalanceByCustomerDTO();
            dto.setCompanyName(customerUserInfo.getCompanyName());

            BigDecimal saleAmount = new BigDecimal("0.00");
            BigDecimal receiveBalance = new BigDecimal("0.00");
            BigDecimal repayAmount = new BigDecimal("0.00");

            String strSaleAmount = m1.get(customerId);
            if (!StringUtil.isEmpty(strSaleAmount)) {
                saleAmount = new BigDecimal(strSaleAmount).setScale(2, RoundingMode.HALF_UP);
            }

            String strRepayAmount = m3.get(customerId);
            if (!StringUtil.isEmpty(strRepayAmount)) {
                repayAmount = new BigDecimal(strRepayAmount).setScale(2, RoundingMode.HALF_UP);
            }

            String strAllSaleAmount = m2.get(customerId);
            if (!StringUtil.isEmpty(strAllSaleAmount)) {
                BigDecimal saleAmountTmp = new BigDecimal(strAllSaleAmount).setScale(2, RoundingMode.HALF_UP);
                BigDecimal repayAmountTmp = new BigDecimal("0.00");
                String strAllRepayAmount = m4.get(customerId);
                if (!StringUtil.isEmpty(strAllRepayAmount)) {
                    repayAmountTmp = new BigDecimal(strAllRepayAmount).setScale(2, RoundingMode.HALF_UP);
                }
                receiveBalance = saleAmountTmp.subtract(repayAmountTmp);
            }

            dto.setReceiveBalance(receiveBalance);
            dto.setRepayAmount(repayAmount);
            dto.setSaleAmount(saleAmount);
            dtoList.add(dto);
        }
        return new Result(dtoList);
    }

    // endregion 应收统计

    // region 回款计划统计
    @Override
    public Result repayPlanByContractPager(RepayPlanForm form) throws Exception {
        PageHelper.startPage(form.getPageCode(), form.getPageSize());
        List<RepayPlanByContractDTO> list = receivableCollectionPlanMapper.repayPlanByContractList(getCurrentUserDepartments(form.getDepartmentId()), form.getPlanStatus(), form.getBeginTime(), form.getEndTime());
        PageInfo<RepayPlanByContractDTO> pageInfo = new PageInfo(list);

        IPage<RepayPlanByContractDTO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(pageInfo.getTotal());
        page.setSize(pageInfo.getSize());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageNum());
        return new Result<>(page);
    }

    @Override
    public Result repayPlanByContractTotal(RepayPlanForm form) throws Exception {
        List<RepayPlanByContractDTO> list = receivableCollectionPlanMapper.repayPlanByContractList(getCurrentUserDepartments(form.getDepartmentId()), form.getPlanStatus(), form.getBeginTime(), form.getEndTime());
        if (list.size() == 0) {
            return new Result();
        }

        BigDecimal totalPlanAmount = list.stream().map(RepayPlanByContractDTO::getPlanAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalBackedAmount = list.stream().map(RepayPlanByContractDTO::getBackedAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalSurplusAmount = list.stream().map(RepayPlanByContractDTO::getSurplusAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);

        return new Result<>(new RepayPlanTotalDTO(totalPlanAmount, totalBackedAmount, totalSurplusAmount));
    }

    @Override
    public Result repayPlanByDepartmentPager(RepayPlanForm form) throws Exception {
        PageHelper.startPage(form.getPageCode(), form.getPageSize());
        List<RepayPlanByDepartmentDTO> list = receivableCollectionPlanMapper.repayPlanByDepartmentList(getCurrentUserDepartments(form.getDepartmentId()), form.getPlanStatus(), form.getBeginTime(), form.getEndTime());
        PageInfo<RepayPlanByDepartmentDTO> pageInfo = new PageInfo(list);

        IPage<RepayPlanByDepartmentDTO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(pageInfo.getTotal());
        page.setSize(pageInfo.getSize());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageNum());
        return new Result<>(page);
    }

    @Override
    public Result repayPlanByDepartmentTotal(RepayPlanForm form) throws Exception {
        List<RepayPlanByDepartmentDTO> list = receivableCollectionPlanMapper.repayPlanByDepartmentList(getCurrentUserDepartments(form.getDepartmentId()), form.getPlanStatus(), form.getBeginTime(), form.getEndTime());
        if (list.size() == 0) {
            return new Result();
        }

        BigDecimal totalPlanAmount = list.stream().map(RepayPlanByDepartmentDTO::getPlanAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalBackedAmount = list.stream().map(RepayPlanByDepartmentDTO::getBackedAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalSurplusAmount = list.stream().map(RepayPlanByDepartmentDTO::getSurplusAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);

        return new Result<>(new RepayPlanTotalDTO(totalPlanAmount, totalBackedAmount, totalSurplusAmount));
    }

    @Override
    public Result repayPlanByCustomerPager(RepayPlanForm form) throws Exception {
        PageHelper.startPage(form.getPageCode(), form.getPageSize());
        List<RepayPlanByCustomerDTO> list = receivableCollectionPlanMapper.repayPlanByCustomerList(getCurrentUserDepartments(form.getDepartmentId()), form.getPlanStatus(), form.getBeginTime(), form.getEndTime());
        PageInfo<RepayPlanByCustomerDTO> pageInfo = new PageInfo(list);

        IPage<RepayPlanByCustomerDTO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(pageInfo.getTotal());
        page.setSize(pageInfo.getSize());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageNum());
        return new Result<>(page);
    }

    @Override
    public Result repayPlanByCustomerDetailPager(RepayPlanForm form) throws Exception {
        PageHelper.startPage(form.getPageCode(), form.getPageSize());
        List<RepayPlanByCustomerDetailDTO> list = receivableCollectionPlanMapper.repayPlanByCustomerDetail(getCurrentUserDepartments(form.getDepartmentId()), form.getPlanStatus(), form.getBeginTime(), form.getEndTime(), form.getCustomerId());
        PageInfo<RepayPlanByCustomerDetailDTO> pageInfo = new PageInfo(list);

        IPage<RepayPlanByCustomerDetailDTO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(pageInfo.getTotal());
        page.setSize(pageInfo.getSize());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageNum());
        return new Result<>(page);
    }

    @Override
    public Result repayPlanByCustomerTotal(RepayPlanForm form) throws Exception {
        List<RepayPlanByCustomerDTO> list = receivableCollectionPlanMapper.repayPlanByCustomerList(getCurrentUserDepartments(form.getDepartmentId()), form.getPlanStatus(), form.getBeginTime(), form.getEndTime());
        if (list.size() == 0) {
            return new Result();
        }

        BigDecimal totalPlanAmount = list.stream().map(RepayPlanByCustomerDTO::getPlanAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalBackedAmount = list.stream().map(RepayPlanByCustomerDTO::getBackedAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalSurplusAmount = list.stream().map(RepayPlanByCustomerDTO::getSurplusAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);

        return new Result<>(new RepayPlanTotalDTO(totalPlanAmount, totalBackedAmount, totalSurplusAmount));
    }

    @Override
    public Result repayPlanByEmployeePager(RepayPlanForm form) throws Exception {
        PageHelper.startPage(form.getPageCode(), form.getPageSize());
        List<RepayPlanByEmployeeDTO> list = receivableCollectionPlanMapper.repayPlanByEmployeeList(getCurrentUserDepartments(form.getDepartmentId()), form.getPlanStatus(), form.getBeginTime(), form.getEndTime());
        PageInfo<RepayPlanByEmployeeDTO> pageInfo = new PageInfo(list);

        IPage<RepayPlanByEmployeeDTO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(pageInfo.getTotal());
        page.setSize(pageInfo.getSize());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageNum());
        return new Result<>(page);
    }

    @Override
    public Result repayPlanByEmployeeTotal(RepayPlanForm form) throws Exception {
        List<RepayPlanByEmployeeDTO> list = receivableCollectionPlanMapper.repayPlanByEmployeeList(getCurrentUserDepartments(form.getDepartmentId()), form.getPlanStatus(), form.getBeginTime(), form.getEndTime());
        if (list.size() == 0) {
            return new Result();
        }

        BigDecimal totalPlanAmount = list.stream().map(RepayPlanByEmployeeDTO::getPlanAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalBackedAmount = list.stream().map(RepayPlanByEmployeeDTO::getBackedAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalSurplusAmount = list.stream().map(RepayPlanByEmployeeDTO::getSurplusAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);

        return new Result<>(new RepayPlanTotalDTO(totalPlanAmount, totalBackedAmount, totalSurplusAmount));
    }

    @Override
    public Result repayPlanByEmployeeDetailPager(RepayPlanForm form) throws Exception {
        PageHelper.startPage(form.getPageCode(), form.getPageSize());
        List<RepayPlanByEmployeeDetailDTO> list = receivableCollectionPlanMapper.repayPlanByEmployeeDetail(getCurrentUserDepartments(form.getDepartmentId()), form.getPlanStatus(), form.getBeginTime(), form.getEndTime(), form.getEmployeeId());
        PageInfo<RepayPlanByEmployeeDetailDTO> pageInfo = new PageInfo(list);

        IPage<RepayPlanByEmployeeDetailDTO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(pageInfo.getTotal());
        page.setSize(pageInfo.getSize());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageNum());
        return new Result<>(page);
    }

    //endregion

    @Override
    public Result overdueByContractPager(RepayPlanForm form) throws Exception {
        PageHelper.startPage(form.getPageCode(), form.getPageSize());
        List<OverdueByContractDTO> list = receivableCollectionPlanMapper.overdueByContractList(getCurrentUserDepartments(form.getDepartmentId()));
        PageInfo<OverdueByContractDTO> pageInfo = new PageInfo(list);

        IPage<OverdueByContractDTO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(pageInfo.getTotal());
        page.setSize(pageInfo.getSize());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageNum());
        return new Result<>(page);
    }

    @Override
    public Result overdueByContractTotal(String departmentId) throws Exception {
        List<OverdueByContractDTO> list = receivableCollectionPlanMapper.overdueByContractList(getCurrentUserDepartments(departmentId));
        if (list.size() == 0) {
            return new Result();
        }

        BigDecimal totalOverdueAmount = list.stream().map(OverdueByContractDTO::getOverdueAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        int totalCustomer = list.stream().collect(Collectors.groupingBy(OverdueByContractDTO::getCustomerName)).size();
        int totalContract = list.size();
        return new Result<>(new OverdueTotalDTO(totalOverdueAmount, totalCustomer, totalContract));
    }

    @Override
    public Result overdueByCustomerPager(RepayPlanForm form) throws Exception {
        PageHelper.startPage(form.getPageCode(), form.getPageSize());
        List<OverdueByCustomerDTO> list = receivableCollectionPlanMapper.overdueByCustomerList(getCurrentUserDepartments(form.getDepartmentId()));
        PageInfo<OverdueByCustomerDTO> pageInfo = new PageInfo(list);

        IPage<OverdueByCustomerDTO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(pageInfo.getTotal());
        page.setSize(pageInfo.getSize());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageNum());
        return new Result<>(page);
    }

    @Override
    public Result overdueByCustomerRank(String departmentId) throws Exception {
        List<OverdueByCustomerDTO> list = receivableCollectionPlanMapper.overdueByCustomerList(getCurrentUserDepartments(departmentId));
        return new Result<>(list);
    }

    @Override
    public Result overdueByEmployeePager(RepayPlanForm form) throws Exception {
        PageHelper.startPage(form.getPageCode(), form.getPageSize());
        List<OverdueByEmployeeDTO> list = receivableCollectionPlanMapper.overdueByEmployeeList(getCurrentUserDepartments(form.getDepartmentId()));
        PageInfo<OverdueByEmployeeDTO> pageInfo = new PageInfo(list);

        IPage<OverdueByEmployeeDTO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(pageInfo.getTotal());
        page.setSize(pageInfo.getSize());
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageNum());
        return new Result<>(page);
    }

    @Override
    public Result overdueByEmployeeRank(String departmentId) throws Exception {
        List<OverdueByEmployeeDTO> list = receivableCollectionPlanMapper.overdueByEmployeeList(getCurrentUserDepartments(departmentId));
        return new Result<>(list);
    }

    private List<String> getCurrentUserDepartments(String departmentId) throws Exception {
        List<String> departmentIds = departmentService.getCurrentUserDepartments();
        if (departmentId == null) {
            return departmentIds;
        }

        if (departmentIds.size() == 0 || !departmentIds.contains(departmentId)) {
            throw new Exception("用户暂无查看该部门的权限");
        }

        departmentIds = new ArrayList<>();
        departmentIds.add(departmentId);
        return departmentIds;
    }
}
