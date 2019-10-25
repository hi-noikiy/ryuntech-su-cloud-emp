package com.ryuntech.saas.outcontroller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.model.FollowupRecord;
import com.ryuntech.saas.api.service.IFollowupRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/minifollowup")
@Api(value = "MiniFollowUpController", tags = {"应收跟进记录"})
public class MiniFollowUpController extends ModuleBaseController{


    @Autowired
    private IFollowupRecordService followupRecordService;

    @PostMapping("/outlist")
    @ApiOperation(value = "分页、条件查询用户列表信息")
    @ApiImplicitParam(name = "followupRecord", value = "查询条件", required = true, dataType = "FollowupRecord", paramType = "body")
    public Result<IPage<FollowupRecord>> list(@RequestBody FollowupRecord followupRecord,  QueryPage queryPage) {
        log.info("followupRecord.getContractId()"+followupRecord.getFollowupId());
        if (StringUtils.isBlank(followupRecord.getContractId())) {
            return new Result<>(OPERATE_ERROR,"合同编号不能为空");
        } else {
            return  followupRecordService.pageList(followupRecord, queryPage);
        }
    }

    /**
     * 添加合同跟进信息
     *
     * @param followupRecord
     * @return
     */
    @PostMapping("/outadd")
    @ApiOperation(value = "添加跟进信息")
    @ApiImplicitParam(name = "followupRecord", value = "合同跟进信息", required = true, dataType = "FollowupRecord", paramType = "body")
    public Result add(@RequestBody FollowupRecord followupRecord) {
        if (StringUtils.isNotBlank(followupRecord.getContractId())){
//            生成主键
            long followupId = generateId();
            followupRecord.setFollowupId(String.valueOf(followupId));
            followupRecord.setFollowupTime(new Date());
            followupRecord.setUpdatedAt(new Date());
            followupRecord.setCreatedAt(new Date());
            boolean save = followupRecordService.save(followupRecord);
            if (save){
                return new Result();
            }else {
                return new Result(OPERATE_ERROR);
            }
        }else {
            return new Result(OPERATE_ERROR);
        }
    }
}
