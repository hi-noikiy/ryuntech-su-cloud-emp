package com.ryuntech.saas.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.SystemTool;
import com.ryuntech.common.utils.redis.JedisUtil;
import com.ryuntech.saas.api.model.PlanOverdueRemind;
import com.ryuntech.saas.api.service.ICompanyService;
import com.ryuntech.saas.api.service.IEmployeeService;
import com.ryuntech.saas.api.service.IPlanOverdueRemindService;
import com.ryuntech.saas.api.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * <p>
 * 应收计划逾期跟进提醒 前端控制器
 * </p>
 *
 * @author antu
 * @since 2019-12-03
 */
@RestController
@RequestMapping("/planOverdueRemind")
public class PlanOverdueRemindController extends ModuleBaseController {

    @Autowired
    private IPlanOverdueRemindService iPlanOverdueRemindService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private IEmployeeService iEmployeeService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 获取配置信息
     * @return
     */
    @GetMapping("/getConfig")
    @ApiOperation(value = "获取计划逾期提醒配置")
    public Result getConfig() {
        String companyId = SystemTool.currentUser(jedisUtil).getCompanyId();
        QueryWrapper<PlanOverdueRemind> queryWrapper =new QueryWrapper<>();
        PlanOverdueRemind planOverdueRemind = iPlanOverdueRemindService.getOne(queryWrapper.eq("company_id", companyId));
        // 配置信息不存在则添加
        if(planOverdueRemind == null) {
            planOverdueRemind = new PlanOverdueRemind();
            String expireId = String.valueOf(generateId());
            planOverdueRemind.setOverdueId(expireId);
            planOverdueRemind.setCompanyId(companyId);
            planOverdueRemind.setFollowPerson("0");
            planOverdueRemind.setDepartHead("0");
            planOverdueRemind.setEmailType("0");
            planOverdueRemind.setWechatType("0");
            planOverdueRemind.setIntervalDay(1);
            planOverdueRemind.setCreateTime(new Date());
            planOverdueRemind.setUpdateTime(new Date());
            iPlanOverdueRemindService.save(planOverdueRemind);
        }

        return new Result(planOverdueRemind);
    }

    /**
     * 配置提醒配置
     * @param planOverdueRemind
     * @return
     */
    @PostMapping("/config")
    @ApiOperation(value = "修改计划逾期提醒配置")
    @ApiImplicitParam(name = "planOverdueRemind", value = "计划逾期提醒配置信息", required = true, dataType = "PlanOverdueRemind", paramType = "body")
    public Result config(@RequestBody PlanOverdueRemind planOverdueRemind) {
        planOverdueRemind.setUpdateTime(new Date());
        boolean b = iPlanOverdueRemindService.updateById(planOverdueRemind);
        if(b) {
            return new Result();
        } else {
            return  new Result(OPERATE_ERROR);
        }
    }

}
