package com.ryuntech.saas.outcontroller;

import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.WeChatIndexDTO;
import com.ryuntech.saas.api.helper.constant.PlanConstant;
import com.ryuntech.saas.api.model.Index;
import com.ryuntech.saas.api.model.WeChatIndex;
import com.ryuntech.saas.api.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 对外小程序首页接口
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/outindex")
@Api(value = "IndexController", tags = {"对外首页信息接口"})
public class IndexController extends ModuleBaseController{
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IUserWechatService userWeChatService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    IEmployeeService employeeService;


    @Autowired
    IndexService indexService;

    /**
     * 首页数据展示
     *
     * @return
     */
    @PostMapping("/reportdata")
    @ApiOperation(value = "首页数据简报展示")
    public Result<Index> index(WeChatIndexDTO weChatIndexDTO) {
        //获取用户token，从缓存中取出用户数据
        Object employeeId =   redisTemplate.opsForValue().get(weChatIndexDTO.getToken());
        if (employeeId!=null){
             //获取openid,查询用户数据
            WeChatIndex weChatIndex = new WeChatIndex();

            weChatIndexDTO.setEmployeeId(employeeId.toString());
//            总待收金额
            String balanceAmounts = indexService.selectBalanceAmounts(weChatIndexDTO);
            weChatIndex.setBalanceAmounts(balanceAmounts);
            // 获取当月第一天和最后一天
            String firstDay = DateUtil.firstDay();
            String lastDay = DateUtil.lastDay();
            weChatIndexDTO.setStartDate(firstDay);
            weChatIndexDTO.setEndDate(lastDay);
            //            本月新增应收
//            设置状态为未开始
            weChatIndexDTO.setStatus(PlanConstant.NOTSTARTED);
            String contractAmounts = indexService.selectContractAmounts(weChatIndexDTO);
            weChatIndex.setContractAmounts(contractAmounts);
            //          本月回款金额
//            设置为已还款
            weChatIndexDTO.setStatus(PlanConstant.REIMBURSEMENT);
            String collectionAmounts = indexService.selectCollectionAmounts(weChatIndexDTO);
            weChatIndex.setCollectionAmounts(collectionAmounts);


//            逾期未收款数量
            //            设置为已逾期
            weChatIndexDTO.setStatus(PlanConstant.OVERDUED);
            Integer overdueNumber = indexService.selectOverdueNumber(weChatIndexDTO);
            weChatIndex.setOverdueNumber(overdueNumber);
//            逾期未收款金额
            String overdueSum = indexService.selectOverdueSum(weChatIndexDTO);
            weChatIndex.setOverdueSum(overdueSum);

            //获取当前时间
            weChatIndexDTO.setStartDate(DateUtil.formatDate(new Date()));
            //获取7天后的时间
            weChatIndexDTO.setEndDate(DateUtil.formatDate(DateUtil.addDays(new Date(),7)));
//            状态为进行中
            weChatIndexDTO.setStatus(PlanConstant.NOTSTARTED);
//            七天内到期的数量
            Integer expireNumber = indexService.selectExpireNumber(weChatIndexDTO);
            weChatIndex.setExpireNumber(expireNumber);
            //            七天内到期的收款金额
            String expireSum = indexService.selectExpireSum(weChatIndexDTO);
            weChatIndex.setExpireSum(expireSum);

//            本月预计回款
//            设置状态为进行中
            weChatIndexDTO.setStatus(PlanConstant.NOTSTARTED);
//            设置时间为本月第一天和最后一天
            weChatIndexDTO.setStartDate(firstDay);
            weChatIndexDTO.setEndDate(lastDay);
            Integer monthNumber = indexService.selectExpireNumber(weChatIndexDTO);
            weChatIndex.setMonthNumber(monthNumber);
            //            七天内到期的收款金额
            String monthSum = indexService.selectExpireSum(weChatIndexDTO);
            weChatIndex.setMonthSum(monthSum);

            return new Result(weChatIndex);
        }

        return new Result();
    }
}

/*

ryn_employee(职工表)
字段名称	数据类型	非空	字段描述
EMPLOYEE_ID	VARCHAR2(64)	Y	职工编号
USER_ID	VARCHAR2(64)	Y	用户编号
NAME	VARCHAR2(255)	Y	员工姓名
COMPANY_ID 	VARCHAR2(64)	Y	所属公司编号
COMPANY_NAME	VARCHAR2(255)	N	所属公司名称


        CREATE TABLE `RYN_EMPLOYEE` (
          `EMPLOYEE_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '职工编号',
          `USER_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户编号',
          `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '职工姓名',
          `COMPANY_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '公司编号',
          `COMPANY_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '公司姓名',
          PRIMARY KEY (`EMPLOYEE_ID`) USING BTREE
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
*/

