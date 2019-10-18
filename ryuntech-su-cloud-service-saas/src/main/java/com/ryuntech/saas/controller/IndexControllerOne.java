package com.ryuntech.saas.controller;

import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.IndexDTO;
import com.ryuntech.saas.api.model.Index;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.service.IReceivableContractService;
import com.ryuntech.saas.api.service.IndexService;
import com.ryuntech.saas.api.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 首页控制器
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/index")
@Api(value = "SysUserController", tags = {"用户信息管理接口"})
public class IndexControllerOne extends ModuleBaseController {


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private IReceivableContractService iReceivableContractService;
    /**
     * 首页数据展示
     *
     * @return
     */
    @PostMapping("/reportdata")
    @ApiOperation(value = "首页数据简报展示")
    public Result<Index> index(IndexDTO indexDTO) {
        //获取用户数据
        String username = getUserName();
        SysUser user = sysUserService.findByName(username);
        //用户编号
        String id = user.getId();
        ReceivableContract receivableContract = new ReceivableContract();
        receivableContract.setStaffId(id);
        List<ReceivableContract> receivableContracts =iReceivableContractService.receivableContractList(receivableContract);

        List<String> contractIdList = new ArrayList<>();
        for (ReceivableContract receivablec:receivableContracts){
            String contractId = receivablec.getContractId();
            contractIdList.add(contractId);
        }

        if (indexDTO==null){
            indexDTO = new IndexDTO();
        }
        indexDTO.setContractIdList(contractIdList);
        Index index = indexService.selectBulletin(indexDTO);
        index.setAmounts("6.00");
        index.setBalanceAmounts("8.00");
        index.setContractId("66666666");
        return new Result(index);
    }


}
