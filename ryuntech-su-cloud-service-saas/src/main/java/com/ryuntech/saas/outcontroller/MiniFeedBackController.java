package com.ryuntech.saas.outcontroller;

import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.Feedback;
import com.ryuntech.saas.api.service.IFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/minifeedback")
@Api(value = "MiniFeedBackController", tags = {"反馈信息记录"})
public class MiniFeedBackController extends ModuleBaseController{



    @Autowired
    private IFeedbackService iFeedbackService;

    /**
     * 添加合同跟进信息
     *
     * @param feedBack
     * @return
     */
    @PostMapping("/outadd")
    @ApiOperation(value = "添加反馈信息")
    @ApiImplicitParam(name = "feedBack", value = "反馈信息", required = true, dataType = "FeedBack", paramType = "body")
    public Result add(@RequestBody Feedback feedBack) {
        if (StringUtils.isBlank(feedBack.getFeedbackContent())){
            return new Result<>(OPERATE_ERROR,"反馈内容不能为空");
        }
//        反馈主键
        String feedbackId = String.valueOf(generateId());
        feedBack.setFeedbackId(feedbackId);
        feedBack.setCreatedAt(new Date());
        boolean b = iFeedbackService.saveOrUpdate(feedBack);
        if (b){
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }
}
