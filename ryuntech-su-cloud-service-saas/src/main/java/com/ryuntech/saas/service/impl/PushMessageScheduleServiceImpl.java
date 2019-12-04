package com.ryuntech.saas.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.HttpUtils;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.dto.RiskMonitorPushDTO;
import com.ryuntech.saas.api.form.CustomerMonitorForm;
import com.ryuntech.saas.api.helper.constant.WeChatConstant;
import com.ryuntech.saas.api.mapper.*;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.ICustomerRiskService;
import com.ryuntech.saas.api.service.ITemplateMessageService;
import com.ryuntech.saas.api.service.PushMessageScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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


    @Override
    public void planExpirePush() {

    }

    /**
     * 每天凌晨5点进行
     * @throws Exception
     */
    @Override
    @Scheduled(cron = "0 0 5 * * ?")
    public void riskMonitorPush() throws Exception {
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
            Integer companySize = 0;
            String customer_ids = hashMap.get("customer_ids").toString();
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
                String sysUserId = employees.get(0).getSysUserId();
                SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("sys_user_id", sysUserId));
                String unionId = sysUser.getUnionId();
                UserWechat userWechat = userWechatMapper.selectOne(new QueryWrapper<UserWechat>().eq("union_id", unionId));
                String gongzhonghaoOpenid = userWechat.getGongzhonghaoOpenid();
                SendTemplateReq weixinTemplate=new SendTemplateReq();
                weixinTemplate.setTemplate_id(WeChatConstant.TEMPLATEID);
//         oKvCSv3pfx_wOYq7oXJLQpPJxFSc  oKvCSvz_OO70H8Iy-Wqh4REbVZNs
                weixinTemplate.setTouser(gongzhonghaoOpenid);
                DoctorReplyMsgData doctorReplyMsgData = new DoctorReplyMsgData();
                doctorReplyMsgData.setFrist(new KeyNote().setValue("您好，您监控的"+companySize+"家公司，共发生了"+riskSize+"条风险，请做好防范工作"));
                doctorReplyMsgData.setKeyword1(new KeyNote().setValue("风险监控日报查看跟进"));
                doctorReplyMsgData.setKeyword2(new KeyNote().setValue(riskSize+"条"));
                doctorReplyMsgData.setKeyword3(new KeyNote().setValue(DateUtil.formatDate(new Date())));
                doctorReplyMsgData.setRemake(new KeyNote().setValue("点击查看详情"));
                weixinTemplate.setData(doctorReplyMsgData);
                weixinTemplate.setUrl("https://www.baidu.com");
                weixinTemplate.setJsonContent(JSON.toJSONString(doctorReplyMsgData));
                String url = TOKEN+"&appid=" +WeChatConstant.WXGZHAPPID+"&secret="+WeChatConstant.WXGZHAPPSECRET;
                String content = HttpUtils.Get(url);
                String accessToken = (String) JSON.parseObject(content).get("access_token");
                SendTemplateResponse sendTemplateResponse = iTemplateMessageService.sendTemplateMessage(accessToken, weixinTemplate);
                if (sendTemplateResponse.getErrcode()==0){
                    //推送成功
                    log.info("用户"+gongzhonghaoOpenid+"推送成功");
                }else {
                    log.info("用户"+gongzhonghaoOpenid+"推送失败");
                }
            }


        }

    }

    @Override
    public void overdueRPush() {

    }
}
