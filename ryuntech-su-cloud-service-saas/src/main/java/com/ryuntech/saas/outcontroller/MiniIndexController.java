package com.ryuntech.saas.outcontroller;

import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.CustomerUserInfoDTO;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.dto.WeChatIndexDTO;
import com.ryuntech.saas.api.dto.WeChatIndexDetailDTO;
import com.ryuntech.saas.api.form.ReceivableContractForm;
import com.ryuntech.saas.api.form.WeChatIndexDetailForm;
import com.ryuntech.saas.api.helper.constant.PlanConstant;
import com.ryuntech.saas.api.helper.constant.ReceivableContractConstants;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;


/**
 * 对外小程序首页接口
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/outindex")
@Api(value = "IndexController", tags = {"对外首页信息接口"})
public class MiniIndexController extends ModuleBaseController{
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
    IReceivableContractService iReceivableContractService;


    @Autowired
    IndexService indexService;

    @Autowired
    ICustomerUserInfoService iCustomerUserInfoService;

    /**
     * 首页数据展示
     *
     * @return
     */
    @PostMapping("/outreportdata")
    @ApiOperation(value = "首页数据简报展示")
    public Result<WeChatIndexDTO> index(@RequestBody WeChatIndexDTO weChatIndexDTO) {
        //获取用户token，从缓存中取出用户数据
        Object employeeId =   weChatIndexDTO.getEmployeeId();
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
            weChatIndexDTO.setPlanStartDate(firstDay);
            weChatIndexDTO.setPlanEndDate(lastDay);
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
            weChatIndexDTO.setPlanStartDate(DateUtil.formatDate(new Date()));
            //获取7天后的时间
            weChatIndexDTO.setPlanEndDate(DateUtil.formatDate(DateUtil.addDays(new Date(),7)));
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
            ArrayList<String> objects = new ArrayList<>();
            objects.add(PlanConstant.NOTSTARTED);
            objects.add(PlanConstant.STARTING);
//            设置时间为本月第一天和最后一天
            weChatIndexDTO.setPlanStartDate(firstDay);
            weChatIndexDTO.setPlanEndDate(lastDay);
            weChatIndexDTO.setStatusList(objects);
            Integer monthNumber = indexService.selectExpireNumber(weChatIndexDTO);
            weChatIndex.setMonthNumber(monthNumber);
            //            本月到期的收款金额
            String monthSum = indexService.selectExpireSum(weChatIndexDTO);
            weChatIndex.setMonthSum(monthSum);

            return new Result(weChatIndex);
        }

        return new Result();
    }


    private WeChatIndexDetailDTO getWeChatIndexDetailDTO(
            WeChatIndexDTO weChatIndexDTO,
            WeChatIndexDetailDTO weChatIndexDetailDTO
    ){
        List<ReceivableCollectionPlan> receivableCollectionPlans = indexService.selectExpireList(weChatIndexDTO);
        List<ReceivableContract> receivableContractList = new ArrayList<>();
        List<CustomerUserInfoDTO> customerUserInfoList  = new ArrayList<>();
//                总待收金额
        BigDecimal balanceAmounts = new BigDecimal(0);
        for (ReceivableCollectionPlan receivableCollectionPlan :receivableCollectionPlans){
            String contractId = receivableCollectionPlan.getContractId();
            ReceivableContract byContract = iReceivableContractService.findByContract(new ReceivableContractForm().setContractId(contractId));
//                    客户编号
            String customerId = byContract.getCustomerId();
            CustomerUserInfoDTO customerUserInfoDTO =iCustomerUserInfoService.selectCustomerUserInfoDTO(new CustomerUserInfo().setCustomerId(customerId));
            customerUserInfoList.add(customerUserInfoDTO);

            BigDecimal balanceAmount = new BigDecimal(byContract.getBalanceAmount());
            balanceAmounts  = balanceAmounts.add(balanceAmount);
            byContract.setReceivableCollectionPlan(receivableCollectionPlan);
            receivableContractList.add(byContract);
        }
//                总待收金额
        weChatIndexDetailDTO.setBalanceAmounts(balanceAmounts.toString());
//                合同总数
        weChatIndexDetailDTO.setAllContract(String.valueOf(receivableContractList.size()));
//                客户总数
        if (customerUserInfoList!=null){
            weChatIndexDetailDTO.setAllCustomer(String.valueOf(new HashSet<>(customerUserInfoList).size()));
        }
//                合同列表
        weChatIndexDetailDTO.setOnListContract(receivableContractList);
        return weChatIndexDetailDTO;
    }

    /**
     * 首页数据展示
     *
     * @return
     */
    @PostMapping("/outdataDetail")
    @ApiOperation(value = "首页详情数据展示")
    public Result<WeChatIndexDetailDTO> dataDetail( @RequestBody WeChatIndexDetailForm weChatIndexDetailForm) {
        Object employeeId =   weChatIndexDetailForm.getEmployeeId();
        if (employeeId!=null){
            String monthId = weChatIndexDetailForm.getMonthId();

            //获取openid,查询用户数据
            WeChatIndexDetailDTO weChatIndexDetailDTO = new WeChatIndexDetailDTO();

//            总待收金额
            WeChatIndexDTO weChatIndexDTO = new WeChatIndexDTO();
            weChatIndexDTO.setEmployeeId(employeeId.toString());
            String monthFirstDay="";
            String monthLastDay="";
            if (StringUtils.isNotBlank(monthId)){
                if (monthId.equals("0")){
//                    获取本月
                    monthFirstDay = DateUtil.getMonthFirstDay(new Date());
                    weChatIndexDTO.setStartDate(monthFirstDay);
                    monthLastDay = DateUtil.getMonthLastDay(new Date());
                    weChatIndexDTO.setEndDate(monthLastDay);
                }else {
                    Date dt=new Date();
                    String year=String.format("%tY", dt);
                    String mon="";
                    if (Integer.parseInt(monthId)>0&&Integer.parseInt(monthId)<10){
                        mon = "0"+monthId;
                    }else {
                        mon = monthId;
                    }
                    monthFirstDay = year+"-" + mon + "-01";
                    monthLastDay = year +"-" + mon + "-31";
                }
            }
//            判断类型
            String type = weChatIndexDetailForm.getType();
            if (type.equals("1")){
//               本月新增应收
                monthFirstDay = DateUtil.getMonthFirstDay(new Date());
                weChatIndexDTO.setStartDate(monthFirstDay);
                monthLastDay = DateUtil.getMonthLastDay(new Date());
                weChatIndexDTO.setEndDate(monthLastDay);
//                设置状态未执行中
                weChatIndexDTO.setStatus(ReceivableContractConstants.NOTSTARTED);
                weChatIndexDetailDTO = getWeChatIndexDetailDTO(weChatIndexDTO,weChatIndexDetailDTO);
                return new Result<>(weChatIndexDetailDTO);
            }else if (type.equals("2")){
//               本月回款金额
                monthFirstDay = DateUtil.getMonthFirstDay(new Date());
                monthLastDay = DateUtil.getMonthLastDay(new Date());
                weChatIndexDTO.setPlanStartDate(monthFirstDay);
                weChatIndexDTO.setPlanEndDate(monthLastDay);
                weChatIndexDTO.setStatus(PlanConstant.REIMBURSEMENT);
                weChatIndexDetailDTO = getWeChatIndexDetailDTO(weChatIndexDTO,weChatIndexDetailDTO);
                return new Result<>(weChatIndexDetailDTO);
            }else if (type.equals("3")){
//               到期预警

            }else if (type.equals("4")){
//               逾期列表
                weChatIndexDTO.setStatus(PlanConstant.OVERDUED);
                weChatIndexDetailDTO = getWeChatIndexDetailDTO(weChatIndexDTO,weChatIndexDetailDTO);
                return new Result<>(weChatIndexDetailDTO);
            }else if (type.equals("5")){
                weChatIndexDetailDTO = getWeChatIndexDetailDTO(weChatIndexDTO,weChatIndexDetailDTO);
                return new Result<>(weChatIndexDetailDTO);
            }else if (type.equals("6")){
//              本月预计回款
                monthFirstDay = DateUtil.getMonthFirstDay(new Date());
                monthLastDay = DateUtil.getMonthLastDay(new Date());
                weChatIndexDTO.setPlanStartDate(monthFirstDay);
                weChatIndexDTO.setPlanEndDate(monthLastDay);
                weChatIndexDTO.setStatus(PlanConstant.STARTING);
                ArrayList<String> objects = new ArrayList<>();
                objects.add(PlanConstant.STARTING);
                objects.add(PlanConstant.NOTSTARTED);
                weChatIndexDetailDTO = getWeChatIndexDetailDTO(weChatIndexDTO,weChatIndexDetailDTO);
                return new Result<>(weChatIndexDetailDTO);
            }else if (type.equals("7")){
//              已逾期未收款 /逾期列表
                weChatIndexDTO.setStatus(PlanConstant.OVERDUED);
                weChatIndexDetailDTO = getWeChatIndexDetailDTO(weChatIndexDTO,weChatIndexDetailDTO);
                return new Result<>(weChatIndexDetailDTO);
            }

//            总代收金额
            String balanceAmounts = indexService.selectBalanceAmounts(weChatIndexDTO);
            weChatIndexDetailDTO.setBalanceAmounts(balanceAmounts);
//            合同总数
            weChatIndexDetailForm.setStartDate(monthFirstDay);
            weChatIndexDetailForm.setEndDate(monthLastDay);
            Integer allContract = indexService.totalContractNumber(weChatIndexDetailForm);
            weChatIndexDetailDTO.setAllContract(String.valueOf(allContract));
//            客户总数
            Integer allCustomer = indexService.totalCustomerNumber(weChatIndexDetailForm);
            weChatIndexDetailDTO.setAllCustomer(String.valueOf(allCustomer));
//            按合同显示数据
            List<ReceivableContract> receivableContracts = indexService.totalReceivableSum(weChatIndexDetailForm);
            weChatIndexDetailDTO.setOnListContract(receivableContracts);
            return new Result(weChatIndexDetailDTO);
        }

        return new Result();
    }

    @PostMapping("/outdepartmentlist")
    @ApiOperation(value = "首页部门数据展示")
    public Result<Department> outDepartment(@RequestBody WeChatIndexDetailForm weChatIndexDetailForm) {
        if (StringUtils.isBlank(weChatIndexDetailForm.getCompanyId())){
            return new Result(OPERATE_ERROR,"公司为空");
        }
        List<Department> byDepartment = departmentService.findByDepartment(new Department().setCompanyId(weChatIndexDetailForm.getCompanyId()));
        return new Result(byDepartment);
    }

}

