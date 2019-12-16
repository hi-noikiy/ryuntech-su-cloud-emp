package com.ryuntech.saas.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuntech.common.exception.YkServiceException;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.HttpUtils;
import com.ryuntech.saas.api.dto.ContractPlanMessageRemindDTO;
import com.ryuntech.saas.api.dto.ContractPlanRemindDTO;
import com.ryuntech.saas.api.dto.RiskMonitorPushDTO;
import com.ryuntech.saas.api.form.CustomerMonitorForm;
import com.ryuntech.saas.api.helper.constant.WeChatConstants;
import com.ryuntech.saas.api.helper.enums.MailTemplateNameEnum;
import com.ryuntech.saas.api.mapper.*;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.ITemplateMessageService;
import com.ryuntech.saas.api.service.PushMessageScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static com.ryuntech.saas.api.helper.HttpConstant.TOKEN;

/**
 * @author EDZ
 */

@Slf4j
@Component
public class PushMessageScheduleServiceImpl implements PushMessageScheduleService {

    @Autowired
    private CustomerRiskMapper customerRiskMapper;

    @Autowired
    private CustomerMonitorMapper customerMonitorMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserWechatMapper userWechatMapper;

    @Autowired
    private ITemplateMessageService iTemplateMessageService;

    @Autowired
    private PlanExpireRemindMapper planExpireRemindMapper;


    @Autowired
    private PlanOverdueRemindMapper planOverdueRemindMapper;


    @Override
    public void planExpirePush() {
        List<ContractPlanRemindDTO> contractPlanRemindDTOS = planExpireRemindMapper.queryPlanMessageRemind();
        if (contractPlanRemindDTOS == null || contractPlanRemindDTOS.size() == 0) {
            return;
        }
        /**
         * 有应收计划的部门id
         */
        Set<String> departmentIds = new HashSet<>();
        /**
         * 有应收计划的公司id
          */
        Set<String> companyIds = new HashSet<>();
        /**
         * 有应收计划的员工id
          */
        Set<String> employeeIds = new HashSet<>();
        for (ContractPlanRemindDTO contractPlanRemindDTO : contractPlanRemindDTOS) {
            departmentIds.add(contractPlanRemindDTO.getDepartmentId());
            companyIds.add(contractPlanRemindDTO.getCompanyId());
            employeeIds.add(contractPlanRemindDTO.getEmployeeId());
        }

        for (String companyId : companyIds) {
            QueryWrapper<PlanExpireRemind>
                    queryWrapper = new QueryWrapper<>();
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
                    List<Employee> employees
                            = employeeMapper.queryEmployeeByDepartmentId(departmentId);
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
                        try {
                            sendMessage(employee,
                                    "您好，当前您有"+contractPlanMessageRemindDTOS.size()+"逾期未收款，请做好相关催收工作",WeChatConstants.RISKTEMPLATEID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // 发送至跟进人
            if (planExpireRemind.getFollowPerson().equals("1")) {
                for (String employeeId : employeeIds) {
                    List<ContractPlanMessageRemindDTO> contractPlanMessageRemindDTOS = new ArrayList<>();
                    BigDecimal bigDecimal = new BigDecimal("0");
                    for (ContractPlanRemindDTO contractPlanRemindDTO : contractPlanRemindDTOS) {
                        /**
                         * 当前回款计划对应的合同跟进人是否是该职工
                         */
                        if (!contractPlanRemindDTO.getEmployeeId().equals(employeeId)) {
                            continue;
                        }
                        String date1 = DateUtil.formatDate(contractPlanRemindDTO.getPlanTime());
                        String date2 = DateUtil.getFutureDate(planExpireRemind.getAdvanceDay());
                        /**
                         * 是否是今天到期
                         */
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
                    /**
                     * 没有需要发送的邮件
                     */
                    if (contractPlanMessageRemindDTOS.size() == 0) {
                        continue;
                    }

                    /**
                     * 查询员工对象
                     */
                    Employee employee = employeeMapper.selectById(employeeId);
                    if (null!=employee){
                        try {
                            sendMessage(employee,
                               "您好，当前您有"+contractPlanMessageRemindDTOS.size()+"逾期未收款，请做好相关催收工作",WeChatConstants.OVERDUETEMPLATEID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 每天凌晨5点进行
     * @throws Exception
     */
    @Override
    @Scheduled(cron = "0 0 5 * * ?")
    public void riskMonitorPush() throws Exception {
            //查询待监控的企业所有的员工
        log.info("riskMonitorPush");
        log.info("开始对监控员工进行消息推送");
        CustomerMonitorForm customerMonitorForm = new CustomerMonitorForm();
        customerMonitorForm.setStatus("1");
        customerMonitorForm.setIsWeChat(true);
        List<HashMap<String, String>> hashMaps = customerMonitorMapper
                .selectGroupMonitorByStaffId(customerMonitorForm);
        log.info("开始对监控员工进行消息推送"+hashMaps.toString());
        for (HashMap<String,String> hashMap :hashMaps){
//            一个跟进人对应多个客户
            RiskMonitorPushDTO riskMonitorPushDTO = new RiskMonitorPushDTO();
            String staff_id = hashMap.get("staffId");
            String staff_name = hashMap.get("staffName");
            riskMonitorPushDTO.setStaffId(staff_id);
            riskMonitorPushDTO.setStaffName(staff_name);
            Integer riskSize = 0;
            Integer companySize = 0;
            String customer_ids = hashMap.get("customerIds");
//            查询公司对应的风险
            if (StringUtils.isNotBlank(customer_ids)){
                String[] split = customer_ids.split(",");
                if (split.length!=0){
                    companySize=split.length;
                }
                for (String plit:split){
                    Integer length = customerRiskMapper.selectCount(new QueryWrapper<CustomerRisk>().eq("customer_id", plit));
                    riskSize+=length;
                }
            }
            riskMonitorPushDTO.setRiskSize(riskSize);
            riskMonitorPushDTO.setCompanySize(companySize);

            //发送消息推送事件
            //通过跟进人编号获取uuid对应的openid
            //获取userid
            List<Employee> employees = employeeMapper.selectList(new QueryWrapper<Employee>().eq("employee_id", staff_id));
            if (!employees.isEmpty()){
                sendMessage(employees.get(0),
                        "您好，您监控的"+companySize+"家公司，共发生了"+riskSize+"条风险，请做好防范工作",WeChatConstants.RISKTEMPLATEID);
            }
        }
    }

    @Override
    public void overdueRPush() {
        // 查询所有有回款计划且未还的相关信息
        List<ContractPlanRemindDTO> contractPlanRemindDTOS = planOverdueRemindMapper.queryPlanMessageRemindWx();
        if (contractPlanRemindDTOS == null || contractPlanRemindDTOS.size() == 0) {
            return;
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

        for (String companyId : companyIds) {
            QueryWrapper<PlanOverdueRemind> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_id", companyId);
            PlanOverdueRemind planOverdueRemind = planOverdueRemindMapper.selectOne(queryWrapper);
            // 是否发送邮件
            if (planOverdueRemind.getEmailType().equals("0")) {
                continue;
            }
            // 跟进人和部门负责人都不发
            if (planOverdueRemind.getFollowPerson().equals("0") && planOverdueRemind.getDepartHead().equals("0")) {
                continue;
            }
            // 是否是第一次发送
            if (planOverdueRemind.getLastRemindTime() != null && !planOverdueRemind.getLastRemindTime().toString().equals("")) {
                // 间隔日期是否是今天
                String lastRemindTime = DateUtil.formatDate(planOverdueRemind.getLastRemindTime());
                int days = planOverdueRemind.getIntervalDay();
                String dateAfterInterval = DateUtil.getFutureDate(-days - 1);
                if (!lastRemindTime.equals(dateAfterInterval)) {
                    continue;
                }
            }
            planOverdueRemind.setLastRemindTime(new Date());
            planOverdueRemindMapper.updateById(planOverdueRemind);
            // 发送至部门负责人
            if (planOverdueRemind.getDepartHead().equals("1")) {
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
                        String date2 = DateUtil.formatDate(new Date());
                        Date d1 = DateUtil.parseDate(date1);
                        Date d2 = DateUtil.parseDate(date2);
                        // 是否逾期
                        if(!d1.before(d2)) {
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
                        contractPlanMessageRemindDTO.setOverdueDays(DateUtil.differentDaysByDate(d1, d2));
                        contractPlanMessageRemindDTOS.add(contractPlanMessageRemindDTO);
                        BigDecimal bigDecimal1 = new BigDecimal(contractPlanRemindDTO.getSurplusAmount());
                        bigDecimal = bigDecimal.add(bigDecimal1);
                    }
                    // 没有需要发送的邮件
                    if (contractPlanMessageRemindDTOS.size() == 0) {
                        continue;
                    }
                    for (Employee employee : employees) {
                        try {
                            sendMessage(employee,
                                    "您好，当前您有"+contractPlanMessageRemindDTOS.size()+"逾期未收款，请做好相关催收工作",WeChatConstants.EXPIRETEMPLATEID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // 发送至跟进人
            if (planOverdueRemind.getFollowPerson().equals("1")) {
                for (String employeeId : employeeIds) {
                    List<ContractPlanMessageRemindDTO> contractPlanMessageRemindDTOS = new ArrayList<>();
                    BigDecimal bigDecimal = new BigDecimal("0");
                    for (ContractPlanRemindDTO contractPlanRemindDTO : contractPlanRemindDTOS) {
                        // 当前回款计划对应的合同跟进人是否是该职工
                        if (!contractPlanRemindDTO.getEmployeeId().equals(employeeId)) {
                            continue;
                        }
                        String date1 = DateUtil.formatDate(contractPlanRemindDTO.getPlanTime());
                        String date2 = DateUtil.formatDate(new Date());
                        Date d1 = DateUtil.parseDate(date1);
                        Date d2 = DateUtil.parseDate(date2);
                        // 是否逾期
                        if(!d1.before(d2)) {
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
                        contractPlanMessageRemindDTO.setOverdueDays(DateUtil.differentDaysByDate(d1, d2));
                        contractPlanMessageRemindDTOS.add(contractPlanMessageRemindDTO);
                        BigDecimal bigDecimal1 = new BigDecimal(contractPlanRemindDTO.getSurplusAmount());
                        bigDecimal = bigDecimal.add(bigDecimal1);
                    }
                    // 没有需要发送的邮件
                    if (contractPlanMessageRemindDTOS.size() == 0) {
                        continue;
                    }

                    /**
                     * 查询员工对象
                     */
                    Employee employee = employeeMapper.selectById(employeeId);
                    try {
                        sendMessage(employee,
                                "您好，有回款计划即将在"+contractPlanMessageRemindDTOS.size()+"到期",WeChatConstants.EXPIRETEMPLATEID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public void sendMessage(Employee employee,String  keyword1,String templateId) throws Exception {
        String sysUserId = employee.getSysUserId();
        SysUser sysUser = sysUserMapper.selectOne(
                new QueryWrapper<SysUser>()
                        .eq("sys_user_id", sysUserId));
        String unionId = sysUser.getUnionId();
        UserWechat userWechat = userWechatMapper.selectOne(new QueryWrapper<UserWechat>().eq("union_id", unionId));
        String gongzhonghaoOpenid = userWechat.getGongzhonghaoOpenid();
        SendTemplateReq weixinTemplate=new SendTemplateReq();
        weixinTemplate.setTemplate_id(templateId);
        weixinTemplate.setTouser(gongzhonghaoOpenid);
        DoctorReplyMsgData doctorReplyMsgData = new DoctorReplyMsgData();
        doctorReplyMsgData.setKeyword1(new KeyNote().setValue(keyword1));
        doctorReplyMsgData.setKeyword2(new KeyNote().setValue(DateUtil.formatDate(new Date())));
        TreeMap<String, String> miniprograms = new TreeMap<>();
        miniprograms.put("appid",WeChatConstants.MINIAPPID);
        /**
         * 注意，这里是支持传参的！！！
         */
        miniprograms.put("pagepath","pages/loading/index");
        weixinTemplate.setMiniprograms(miniprograms);
        weixinTemplate.setData(doctorReplyMsgData);
        weixinTemplate.setUrl("http://mp.weixin.qq.com");
        weixinTemplate.setJsonContent(JSON.toJSONString(doctorReplyMsgData));
        String url = TOKEN+"&appid=" + WeChatConstants.WXGZHAPPID+"&secret="+ WeChatConstants.WXGZHAPPSECRET;
        String content = HttpUtils.Get(url);
        String accessToken = (String) JSON.parseObject(content).get("access_token");
        log.info("微信推送返回消息accessToken"+accessToken);
        SendTemplateResponse sendTemplateResponse = iTemplateMessageService.sendTemplateMessage(accessToken, weixinTemplate);
        log.info("微信推送返回消息"+sendTemplateResponse.toString());
        log.info("微信推送返回消息errmsg"+sendTemplateResponse.getErrmsg());
        log.info("微信推送返回消息errcode"+sendTemplateResponse.getErrcode());
        if (sendTemplateResponse.getErrcode()==0){
            //推送成功
            log.info("用户"+gongzhonghaoOpenid+"推送成功");
        }else {
            log.info("用户"+gongzhonghaoOpenid+"推送失败");
        }
    }

}
