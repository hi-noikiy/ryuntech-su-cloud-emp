package com.ryuntech.saas.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.dto.RiskMonitorPushDTO;
import com.ryuntech.saas.api.form.CustomerMonitorForm;
import com.ryuntech.saas.api.mapper.*;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.ICustomerRiskService;
import com.ryuntech.saas.api.service.PushMessageScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

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
    private CustomerUserInfoMapper customerUserInfoMapper;


    @Autowired
    private EmployeeMapper employeeMapper;


    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserWechatMapper userWechatMapper;


    @Override
    public void planExpirePush() {

    }

    @Override
    public void riskMonitorPush() {
            //查询待监控的企业所有的员工
        List<HashMap<String, String>> hashMaps = customerMonitorMapper.selectGroupMonitorByStaffId(new CustomerMonitorForm());
        for (HashMap<String,String> hashMap :hashMaps){
//            一个跟进人对应多个客户
            RiskMonitorPushDTO riskMonitorPushDTO = new RiskMonitorPushDTO();
            String staff_id = hashMap.get("staff_id");
            String staff_name = hashMap.get("staff_name");
            riskMonitorPushDTO.setStaffId(staff_id);
            riskMonitorPushDTO.setStaffName(staff_name);
            Integer riskSize = 0;
            String customer_ids = hashMap.get("customer_ids").toString();
//            查询公司对应的风险
            if (StringUtils.isNotBlank(customer_ids)){
                String[] split = customer_ids.split(",");
                for (String plit:split){
                    Integer length = customerRiskMapper.selectCount(new QueryWrapper<CustomerRisk>().eq("customer_id", plit));
                    riskSize+=length;
                }
            }
            riskMonitorPushDTO.setRiskSize(riskSize);

            //发送消息推送事件
            //通过跟进人编号获取uuid对应的openid
            //获取userid
            List<Employee> employees = employeeMapper.selectList(new QueryWrapper<Employee>().eq("employee_id", staff_id));
            if (!employees.isEmpty()){
                String sysUserId = employees.get(0).getSysUserId();
                SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("sys_user_id", sysUserId));
                String unionId = sysUser.getUnionId();
                UserWechat userWechat = userWechatMapper.selectOne(new QueryWrapper<UserWechat>().eq("union_id", unionId));
                String gongzhonghaoOpenid = userWechat.getGongzhonghaoOpenid();

            }


        }

    }

    @Override
    public void overdueRPush() {

    }
}
