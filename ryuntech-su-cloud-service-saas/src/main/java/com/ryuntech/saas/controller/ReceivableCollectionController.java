package com.ryuntech.saas.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.ReceivableCollection;
import com.ryuntech.saas.api.service.IReceivableCollectionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * <p>
 * 回款表 前端控制器
 * </p>
 *
 * @author antu
 * @since 2019-09-27
 */
@RestController
@RequestMapping("/collection")
public class ReceivableCollectionController extends ModuleBaseController {
    @Autowired
    private IReceivableCollectionService iReceivableCollectionService;


    /**
     * 删除用户信息
     *
     * @param collectionId
     * @return
     */
    @DeleteMapping("/{contractId}")
    @ApiOperation(value = "删除回款")
    @ApiImplicitParam(name = "contractId", value = "回款编号", required = true, dataType = "String")
    public Result delete(@PathVariable String collectionId) {
        boolean isSuccess =iReceivableCollectionService.removeById(collectionId);
        if (isSuccess){
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

    /**
     * 分页查询列表数据，条件查询
     *
     * @param receivableCollection
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页、条件查询客户列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receivableCollection", value = "查询条件", dataType = "ReceivableCollection", paramType = "body"),
            @ApiImplicitParam(name="queryPage",value="分页信息",dataType="QueryPage", paramType = "body")
    })
    public Result<IPage<ReceivableCollection>> list(@RequestBody ReceivableCollection receivableCollection, QueryPage queryPage) {
        return iReceivableCollectionService.selectPageList(receivableCollection,queryPage);
    }
}
