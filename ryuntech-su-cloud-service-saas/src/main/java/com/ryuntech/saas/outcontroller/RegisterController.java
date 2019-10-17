package com.ryuntech.saas.outcontroller;

import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.WeChatIndexDTO;
import com.ryuntech.saas.api.model.Index;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对外小程序注册接口
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/outregister")
@Api(value = "RegisterController", tags = {"对外小程序注册接口"})
public class RegisterController extends ModuleBaseController {

    /**
     * 注册第一步
     *
     * @return
     */
    @PostMapping("/frist")
    @ApiOperation(value = "注册第一步验证手机号")
    public Result<Index> frist(WeChatIndexDTO weChatIndexDTO) {

        return new Result();
    }

    /**
     * 注册第二步
     *
     * @return
     *//*
    @PostMapping("/second")
    @ApiOperation(value = "注册第二步验证手机号")
    public Result<Index> frist(WeChatIndexDTO weChatIndexDTO) {

    }*/
}
