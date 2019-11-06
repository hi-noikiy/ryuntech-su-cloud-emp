package com.ryuntech.saas.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.service.IReceivableCollectionPlanService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author antu
 * @since 2019-09-29
 */
@RestController
@RequestMapping("/collectionPlan")
public class ReceivableCollectionPlanController extends ModuleBaseController{



    @Autowired
    private IReceivableCollectionPlanService iReceivableCollectionPlanService;
    /**
     * 分页查询列表数据，条件查询
     *
     * @param receivableCollectionPlan
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页、条件查询还款计划列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receivableCollectionPlan", value = "查询条件", dataType = "ReceivableCollectionPlan", paramType = "body"),
            @ApiImplicitParam(name="queryPage",value="分页信息",dataType="QueryPage", paramType = "body")
    })
    public Result<IPage<ReceivableCollectionPlan>> list(@RequestBody ReceivableCollectionPlan receivableCollectionPlan, QueryPage queryPage) {
        return iReceivableCollectionPlanService.selectPageList(receivableCollectionPlan,queryPage);
    }

    @PostMapping("/insertBatch")
    @ApiOperation(value = "批量添加回款计划")
    @ApiImplicitParam(name = "receivableCollectionPlan", value = "回款计划信息", required = true, dataType = "ReceivableCollectionPlan", paramType = "body")
    public Result insertBatch(@RequestBody List<ReceivableCollectionPlan> receivableCollectionPlans) {
        for(ReceivableCollectionPlan rcp : receivableCollectionPlans) {
            rcp.setPlanId(String.valueOf(generateId()));
        }
        boolean b = iReceivableCollectionPlanService.insertBatch(receivableCollectionPlans);
        if (b){
            //回款计划插入成功
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

}
