package com.ryuntech.saas.outcontroller;


import com.google.gson.Gson;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.SysUserDTO;
import com.ryuntech.saas.api.form.WxAuthForm;
import com.ryuntech.saas.api.helper.CheckoutUtil;
import com.ryuntech.saas.api.helper.HttpConstant;
import com.ryuntech.saas.api.helper.constant.WeChatConstant;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;


/**
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/miniwxsendmsg")
@Api(value = "MiniWxSendMsgController", tags = {"小程序消息接收推送接口"})
public class MiniWxSendMsgController extends ModuleBaseController{

    @Autowired
    ITemplateMessageService iTemplateMessageService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    IUserWechatService iUserWechatService;

    /**
     * 微信token信息验证
     *
     * @return
     */
    @GetMapping("/outcheckSignature")
    @ApiOperation(value = "微信token信息验证")
    private String valid(WxToken wxToken){
        log.info("checkSignature");
        log.info("开始验证token信息");
        log.info("开始验证wxToken"+wxToken.getSignature());
        log.info("开始验证wxToken.getEchostr"+wxToken.getEchostr());
        if (wxToken.getSignature() != null && CheckoutUtil.checkSignature(wxToken.getSignature(), wxToken.getTimestamp(), wxToken.getNonce())) {
           return wxToken.getEchostr();
        }
        return null;
    }

    /**
     * 微信token信息验证
     *
     * @return
     */
    @GetMapping("/sendTemplateMessage")
    @ApiOperation(value = "微信发送模板消息")
    public SendTemplateResponse sendTemplateMessage(String accessToken, SendTemplateReq weixinTemplate) throws Exception {
//        open_id ='oxzcO5KaNB6_ZLNUBOvN3_phHIC0'
        SendTemplateResponse sendTemplateResponse = iTemplateMessageService.sendTemplateMessage(accessToken, weixinTemplate);
        return sendTemplateResponse;
    }


    /**
     * 小程序获取openid
     *
     * @param wxAuthForm
     * @return
     */
    @PostMapping("/outAuth")
    @ApiOperation(value = "用户注册")
    @ApiImplicitParam(name = "code", value = "小程序登录的code", required = true, dataType = "WxAuthForm", paramType = "body")
    public Result auth(@RequestBody WxAuthForm wxAuthForm)  {
        if (StringUtils.isBlank(wxAuthForm.getEmployeeId())){
            return new Result(OPERATE_ERROR,"员工编号不能为空");
        }
        log.info("wxAuthForm.getCode()="+wxAuthForm.getCodeValue());
        String url = HttpConstant.JSCODE2SESSION+"?appid="
                + WeChatConstant.APPID+
                "&secret="+WeChatConstant.APPSECRET+
                "&js_code="+ wxAuthForm.getCodeValue() +
                "&grant_type=authorization_code";
        //发送post请求读取调用微信接口获取openid用户唯一标识
        RestTemplate restTemplate = new RestTemplate();
        log.info("url++===="+url);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
        {
            String sessionData = responseEntity.getBody();
            log.info("sessionData===="+sessionData);
            //解析从微信服务器获得的openid和session_key;
            WeChatSession weChatSession = new Gson().fromJson(sessionData,WeChatSession.class);
            //获取用户的唯一标识
            String openid = weChatSession.getOpenid();
            String unionid = weChatSession.getUnionid();
            log.info("openid",openid);
            //获取会话秘钥
            String session_key = weChatSession.getSession_key();
            log.info("session_key",session_key);
            //判断openid是否存在用户
            List<Employee> employees = iEmployeeService.selectByEmployeeList(new Employee().setEmployeeId(wxAuthForm.getEmployeeId()));
            String userId="";
            if (!employees.isEmpty()){
                userId = employees.get(0).getUserId();
            }

            SysUser sysUser = sysUserService.selectByUserDTO(new SysUserDTO().setId(userId));
            if (StringUtils.isBlank(sysUser.getOpenId())){
                //开始更新用户的对象信息
                sysUser.setOpenId(openid);
                sysUser.setUnionId(unionid);
                sysUser.setAvatar(wxAuthForm.getAvatar());


//                创建微信用户对象
                UserWechat userWechat = new UserWechat();
                userWechat.setUserId(sysUser.getId());
                String weChatId = String.valueOf(generateId());
                userWechat.setId(weChatId);
                userWechat.setCity(wxAuthForm.getCity());
                userWechat.setCountry(wxAuthForm.getCountry());
                userWechat.setProvince(wxAuthForm.getProvince());
                userWechat.setHeadimgurl(wxAuthForm.getAvatar());
                userWechat.setCreatedAt(new Date());
                userWechat.setUpdatedAt(new Date());
                userWechat.setUnionId(unionid);
                userWechat.setNickname(wxAuthForm.getNickname());
                userWechat.setMiniprogramOpenid(openid);
                userWechat.setSex(Integer.parseInt(wxAuthForm.getSex()));
                boolean b = sysUserService.updateById(sysUser);
                if (b){
                    boolean save = iUserWechatService.save(userWechat);
                    if (save){
                        return new Result(sysUser);
                    } else {
                        return new Result(OPERATE_ERROR,"用户更新失败");
                    }
                }else {
                    return new Result(OPERATE_ERROR,"用户更新失败");
                }
            }
        }
        return new Result();
    }

}
