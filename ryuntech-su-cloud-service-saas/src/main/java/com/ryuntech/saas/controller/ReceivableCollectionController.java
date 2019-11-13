package com.ryuntech.saas.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.form.ReceivableCollectionConditionForm;
import com.ryuntech.saas.api.model.ReceivableCollection;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;
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
     * 删除回款信息
     *
     * @param collectionId
     * @return
     */
    @DeleteMapping("/{collectionId}")
    @ApiOperation(value = "删除回款")
    @ApiImplicitParam(name = "collectionId", value = "回款编号", required = true, dataType = "String")
    public Result delete(@PathVariable String collectionId) {
        boolean isSuccess =iReceivableCollectionService.removeById(collectionId);
        if (isSuccess){
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

    /**
     * 回款信息作废
     *
     * @param collectionId
     * @return
     */
    @PostMapping("/collectionzuofei/{collectionId}")
    @ApiOperation(value = "回款作废")
    @ApiImplicitParam(name = "collectionId", value = "回款作废编号", required = true, dataType = "String")
    public Result zuofei(@PathVariable String collectionId) {
        ReceivableCollection receivableCollection = new ReceivableCollection();
        receivableCollection.setCollectionId(collectionId);
        receivableCollection.setStatus("0");
        boolean isSuccess =iReceivableCollectionService.updateById(receivableCollection);
        if (isSuccess){
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

    /**
     * 分页查询列表数据，条件查询
     *
     * @param receivableCollectionConditionForm
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页、条件查询回款列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receivableCollection", value = "查询条件", dataType = "ReceivableCollection", paramType = "body"),
            @ApiImplicitParam(name="queryPage",value="分页信息",dataType="QueryPage", paramType = "body")
    })
    public Result<IPage<ReceivableCollection>> list(@RequestBody ReceivableCollectionConditionForm receivableCollectionConditionForm, QueryPage queryPage) {
        return iReceivableCollectionService.selectPageList(receivableCollectionConditionForm,queryPage);
    }

    /**
     * 添加回款信息
     *
     * @param receivableCollection
     * @return
     */
    @PostMapping("/collectionadd")
    @ApiOperation(value = "添加回款信息")
    @ApiImplicitParam(name = "receivableCollection", value = "回款实体信息", required = true, dataType = "ReceivableCollection", paramType = "body")
    public Result add(@RequestBody ReceivableCollection receivableCollection) {
        receivableCollection.setCollectionId(String.valueOf(generateId()));
        receivableCollection.setCreateBy(getUserName());
        ReceivableCollectionPlan receivableCollectionPlan = new ReceivableCollectionPlan();
        ReceivableContract receivableContract = new ReceivableContract();
        boolean b = iReceivableCollectionService.addReceivableCollection(receivableCollectionPlan, receivableCollection, receivableContract);
        if(b) {
            return new Result();
        } else {
            return new Result(OPERATE_ERROR);
        }

    }

}
