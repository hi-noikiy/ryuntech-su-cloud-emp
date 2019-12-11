package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.saas.api.dto.ContractPlanMessageRemindDTO;
import com.ryuntech.saas.api.dto.ContractPlanRemindDTO;
import com.ryuntech.saas.api.helper.enums.MailTemplateNameEnum;
import com.ryuntech.saas.api.mapper.EmployeeMapper;
import com.ryuntech.saas.api.mapper.PlanExpireRemindMapper;
import com.ryuntech.saas.api.model.Email;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.model.PlanExpireRemind;
import com.ryuntech.saas.api.service.IPlanExpireRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 应收计划到期提醒 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-12-03
 */
@Service
public class PlanExpireRemindServiceImpl extends ServiceImpl<PlanExpireRemindMapper, PlanExpireRemind> implements IPlanExpireRemindService {

    @Autowired
    private PlanExpireRemindMapper planExpireRemindMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Email> getEmails() {
        // 查询所有有回款计划且未还的相关信息
        List<ContractPlanRemindDTO> contractPlanRemindDTOS = planExpireRemindMapper.queryPlanMessageRemind();
        if (contractPlanRemindDTOS == null || contractPlanRemindDTOS.size() == 0) {
            return null;
        }

//        List<Map<String, String>> map1 =   planMessageRemindDTOS.parallelStream().map(p -> {
//            Map<String, String> map = new HashMap<>();
//            map.put(p.getEmployeeId(), p.getCompanyId());
//            return map;
//        }).distinct().collect(Collectors.toList());

        // 有应收计划的部门id
        Set<String> departmentIds = new HashSet<>();
        // 有应收计划的公司id
        Set<String> companyIds = new HashSet<>();
        // 有应收计划的员工id
        Set<String> employeeIds = new HashSet<>();
        for (ContractPlanRemindDTO contractPlanRemindDTO : contractPlanRemindDTOS) {
            departmentIds.add(contractPlanRemindDTO.getDepartmentId());
            companyIds.add(contractPlanRemindDTO.getCompanyId());
            employeeIds.add(contractPlanRemindDTO.getEmployeeId());
        }

        List<Email> emailList = new ArrayList<>();
        for (String companyId : companyIds) {
            QueryWrapper<PlanExpireRemind> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_id", companyId);
            PlanExpireRemind planExpireRemind = planExpireRemindMapper.selectOne(queryWrapper);
            // 是否发送邮件
            if (planExpireRemind.getEmailType().equals("0")) {
                continue;
            }
            // 跟进人和部门负责人都不发
            if (planExpireRemind.getFollowPerson().equals("0") && planExpireRemind.getDepartHead().equals("0")) {
                continue;
            }
            // 发送至部门负责人
            if (planExpireRemind.getDepartHead().equals("1")) {
                for (String departmentId : departmentIds) {
                    List<Employee> employees = employeeMapper.queryEmployeeByDepartmentId(departmentId);
                    List<ContractPlanMessageRemindDTO> contractPlanMessageRemindDTOS = new ArrayList<>();
                    BigDecimal bigDecimal = new BigDecimal("0");
                    for (ContractPlanRemindDTO contractPlanRemindDTO : contractPlanRemindDTOS) {
                        // 非当前部门
                        if (!contractPlanRemindDTO.getDepartmentId().equals(departmentId)) {
                            continue;
                        }
                        String date1 = DateUtil.formatDate(contractPlanRemindDTO.getPlanTime());
                        String date2 = DateUtil.getFutureDate(planExpireRemind.getAdvanceDay());
                        // 是否是今天到期
                        if(!date1.equals(date2)) {
                            continue;
                        }
                        ContractPlanMessageRemindDTO contractPlanMessageRemindDTO = new ContractPlanMessageRemindDTO();
                        contractPlanMessageRemindDTO.setContractName(contractPlanRemindDTO.getContractName());
                        contractPlanMessageRemindDTO.setCustomerName(contractPlanRemindDTO.getCustomerName());
                        contractPlanMessageRemindDTO.setContractAmount(contractPlanRemindDTO.getContractAmount());
                        contractPlanMessageRemindDTO.setStaffName(contractPlanRemindDTO.getName());
                        contractPlanMessageRemindDTO.setDepartment(contractPlanRemindDTO.getDepartmentName());
                        contractPlanMessageRemindDTO.setBackedAmount(contractPlanRemindDTO.getBackedAmount());
                        contractPlanMessageRemindDTO.setPlanAmount(contractPlanRemindDTO.getPlanAmount());
                        contractPlanMessageRemindDTO.setPlanTime(DateUtil.formatDate(contractPlanRemindDTO.getPlanTime()));
                        contractPlanMessageRemindDTO.setPlanPeriod(contractPlanRemindDTO.getPlanPeriod() + "/" + contractPlanRemindDTO.getTotalPlanPeriodes());
                        contractPlanMessageRemindDTOS.add(contractPlanMessageRemindDTO);
                        BigDecimal bigDecimal1 = new BigDecimal(contractPlanRemindDTO.getSurplusAmount());
                        bigDecimal = bigDecimal.add(bigDecimal1);
                    }
                    // 没有需要发送的邮件
                    if (contractPlanMessageRemindDTOS.size() == 0) {
                        continue;
                    }
                    for (Employee employee : employees) {
                        Email email = new Email();
                        email.setEmail(employee.getEmail());
                        email.setSubject("应收计划到期提醒--" + DateUtil.formatDate(new Date()));
                        email.setTemplate(MailTemplateNameEnum.PlanExpiresEmail.getCode());
                        Map<String, Object> mab = new HashMap<>();
                        mab.put("username", employee.getName());
                        mab.put("count", contractPlanMessageRemindDTOS.size());
                        mab.put("amountOfMoney", bigDecimal.toString());
                        mab.put("days", planExpireRemind.getAdvanceDay());
                        mab.put("dueCollectionPlans", contractPlanMessageRemindDTOS);
                        email.setParams(mab);
                        emailList.add(email);

                        // 如果部门负责人发送了邮件，且当前部门负责人也有应收跟进计划，则不另外单独发送自己的
                        if (employeeIds.contains(employee.getEmployeeId())) {
                            employeeIds.remove(employee.getEmployeeId());
                        }
                    }
                }
            }
            // 发送至跟进人
            if (planExpireRemind.getFollowPerson().equals("1")) {
                for (String employeeId : employeeIds) {
                    List<ContractPlanMessageRemindDTO> contractPlanMessageRemindDTOS = new ArrayList<>();
                    BigDecimal bigDecimal = new BigDecimal("0");
                    String emailNumber = null;
                    String employeeName = null;
                    for (ContractPlanRemindDTO contractPlanRemindDTO : contractPlanRemindDTOS) {
                        // 当前回款计划对应的合同跟进人是否是该职工
                        if (!contractPlanRemindDTO.getEmployeeId().equals(employeeId)) {
                            continue;
                        }
                        String date1 = DateUtil.formatDate(contractPlanRemindDTO.getPlanTime());
                        String date2 = DateUtil.getFutureDate(planExpireRemind.getAdvanceDay());
                        // 是否是今天到期
                        if(!date1.equals(date2)) {
                            continue;
                        }
                        ContractPlanMessageRemindDTO contractPlanMessageRemindDTO = new ContractPlanMessageRemindDTO();
                        contractPlanMessageRemindDTO.setContractName(contractPlanRemindDTO.getContractName());
                        contractPlanMessageRemindDTO.setCustomerName(contractPlanRemindDTO.getCustomerName());
                        contractPlanMessageRemindDTO.setContractAmount(contractPlanRemindDTO.getContractAmount());
                        contractPlanMessageRemindDTO.setStaffName(contractPlanRemindDTO.getName());
                        contractPlanMessageRemindDTO.setDepartment(contractPlanRemindDTO.getDepartmentName());
                        contractPlanMessageRemindDTO.setBackedAmount(contractPlanRemindDTO.getBackedAmount());
                        contractPlanMessageRemindDTO.setPlanAmount(contractPlanRemindDTO.getPlanAmount());
                        contractPlanMessageRemindDTO.setPlanTime(DateUtil.formatDate(contractPlanRemindDTO.getPlanTime()));
                        contractPlanMessageRemindDTO.setPlanPeriod(contractPlanRemindDTO.getPlanPeriod() + "/" + contractPlanRemindDTO.getTotalPlanPeriodes());
                        contractPlanMessageRemindDTOS.add(contractPlanMessageRemindDTO);
                        BigDecimal bigDecimal1 = new BigDecimal(contractPlanRemindDTO.getSurplusAmount());
                        bigDecimal = bigDecimal.add(bigDecimal1);
                        emailNumber = contractPlanRemindDTO.getEmail();
                        employeeName = contractPlanRemindDTO.getName();
                    }
                    // 没有需要发送的邮件
                    if (contractPlanMessageRemindDTOS.size() == 0) {
                        continue;
                    }
                    Email email = new Email();
                    email.setEmail(emailNumber);
                    email.setSubject("应收计划到期提醒--" + DateUtil.formatDate(new Date()));
                    email.setTemplate(MailTemplateNameEnum.PlanExpiresEmail.getCode());
                    Map<String, Object> mab = new HashMap<>();
                    mab.put("username", employeeName);
                    mab.put("count", contractPlanMessageRemindDTOS.size());
                    mab.put("amountOfMoney", bigDecimal.toString());
                    mab.put("days", planExpireRemind.getAdvanceDay());
                    mab.put("dueCollectionPlans", contractPlanMessageRemindDTOS);
                    email.setParams(mab);
                    emailList.add(email);
                }
            }
        }

        return emailList;
    }
}
