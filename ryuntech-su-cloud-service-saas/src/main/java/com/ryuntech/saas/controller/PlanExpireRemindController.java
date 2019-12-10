package com.ryuntech.saas.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.model.PlanExpireRemind;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.service.IEmployeeService;
import com.ryuntech.saas.api.service.IPlanExpireRemindService;
import com.ryuntech.saas.api.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * <p>
 * 应收计划到期提醒 前端控制器
 * </p>
 *
 * @author antu
 * @since 2019-12-03
 */
@RestController
@RequestMapping("/planExpireRemind")
public class PlanExpireRemindController extends ModuleBaseController {

    @Autowired
    private IPlanExpireRemindService iPlanExpireRemindService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private IEmployeeService iEmployeeService;

    /**
     * 获取配置信息
     * @return
     */
    @GetMapping("/getConfig")
    @ApiOperation(value = "获取计划到期提醒配置")
    public Result getConfig() {
        String username = getUserName();
        SysUser user = sysUserService.findByName(username);
        QueryWrapper<Employee> queryWrapper1 =new QueryWrapper<>();
        queryWrapper1.eq("sys_user_id", user.getSysUserId());
        Employee employee = iEmployeeService.getOne(queryWrapper1);
        QueryWrapper<PlanExpireRemind> queryWrapper2 =new QueryWrapper<>();
        PlanExpireRemind planExpireRemind = iPlanExpireRemindService.getOne(queryWrapper2.eq("company_id", employee.getCompanyId()));
        // 配置信息不存在则添加
        if(planExpireRemind == null) {
            planExpireRemind = new PlanExpireRemind();
            String expireId = String.valueOf(generateId());
            planExpireRemind.setExpireId(expireId);
            planExpireRemind.setCompanyId(employee.getCompanyId());
            planExpireRemind.setFollowPerson("0");
            planExpireRemind.setDepartHead("0");
            planExpireRemind.setEmailType("0");
            planExpireRemind.setWechatType("0");
            planExpireRemind.setAdvanceDay(1);
            planExpireRemind.setCreateTime(new Date());
            planExpireRemind.setUpdateTime(new Date());
            iPlanExpireRemindService.save(planExpireRemind);
        }

        return new Result(planExpireRemind);
    }

    /**
     * 配置提醒配置
     * @param planExpireRemind
     * @return
     */
    @PostMapping("/config")
    @ApiOperation(value = "修改计划到期提醒配置")
    @ApiImplicitParam(name = "planExpireRemind", value = "计划到期提醒配置信息", required = true, dataType = "PlanExpireRemind", paramType = "body")
    public Result config(@RequestBody PlanExpireRemind planExpireRemind) {
        planExpireRemind.setUpdateTime(new Date());
        boolean b = iPlanExpireRemindService.updateById(planExpireRemind);
        if(b) {
            return new Result();
        } else {
            return  new Result(OPERATE_ERROR);
        }
    }

}
