package com.ryuntech.saas.outcontroller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ryuntech.common.utils.HttpUtils;
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
import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;
import static com.ryuntech.saas.api.helper.HttpConstant.INFOUNIONID;
import static com.ryuntech.saas.api.helper.HttpConstant.TOKEN;
import static com.ryuntech.saas.api.helper.constant.WeChatConstant.*;


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
    @GetMapping("/outsendTemplateMessage")
    @ApiOperation(value = "微信发送模板消息")
    public SendTemplateResponse sendTemplateMessage(String accessToken, SendTemplateReq weixinTemplate) throws Exception {
//        open_id ='oxzcO5KaNB6_ZLNUBOvN3_phHIC0'
//        测试模板数据
        weixinTemplate=new SendTemplateReq();
        weixinTemplate.setTemplate_id("UXxkjX3AkOuglrNm24sglwCTXATTjI7tkb_JuN9Ka7U");
//         oKvCSv3pfx_wOYq7oXJLQpPJxFSc  oKvCSvz_OO70H8Iy-Wqh4REbVZNs
        weixinTemplate.setTouser("oKvCSv3pfx_wOYq7oXJLQpPJxFSc");
        DoctorReplyMsgData doctorReplyMsgData = new DoctorReplyMsgData();
        doctorReplyMsgData.setFrist(new KeyNote().setValue("您好，您监控的5家公司，共发生了5条风险，请做好防范工作"));
        doctorReplyMsgData.setKeyword1(new KeyNote().setValue("风险监控日报查看跟进"));
        doctorReplyMsgData.setKeyword2(new KeyNote().setValue("5条"));
        doctorReplyMsgData.setKeyword3(new KeyNote().setValue("2019-09-09"));
        doctorReplyMsgData.setRemake(new KeyNote().setValue("点击查看详情"));
        weixinTemplate.setData(doctorReplyMsgData);
        weixinTemplate.setUrl("https://www.baidu.com");
        weixinTemplate.setJsonContent(JSON.toJSONString(doctorReplyMsgData));
        String url = TOKEN+"&appid=" +WeChatConstant.WXGZHAPPID+"&secret="+WeChatConstant.WXGZHAPPSECRET;
        String content = HttpUtils.Get(url);
        accessToken = (String) JSON.parseObject(content).get("access_token");
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
    @ApiOperation(value = "小程序用户认证")
    @ApiImplicitParam(name = "wxAuthForm", value = "小程序认证的Form", required = true, dataType = "WxAuthForm", paramType = "body")
    public Result auth(@RequestBody WxAuthForm wxAuthForm)  {
        if (StringUtils.isBlank(wxAuthForm.getEmployeeId())){
            return new Result(OPERATE_ERROR,"员工编号不能为空");
        }
        log.info("wxAuthForm.getCode()="+wxAuthForm.getCodeValue());
        String url = HttpConstant.JSCODE2SESSION+"?appid="
                + WeChatConstant.MINIAPPID+
                "&secret="+WeChatConstant.MINIAPPSECRET+
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
            List<Employee> employees = iEmployeeService.selectByEmployeeList(
                    new Employee().setEmployeeId(
                            wxAuthForm.getEmployeeId()));
            String userId="";
            if (!employees.isEmpty()){
                userId = employees.get(0).getSysUserId();
            }

            SysUser sysUser = sysUserService.selectByUserDTO(new SysUserDTO().setId(userId));
            if (StringUtils.isBlank(sysUser.getOpenId())){
                //开始更新用户的对象信息
                sysUser.setOpenId(openid);
                sysUser.setUnionId(unionid);
                sysUser.setAvatar(wxAuthForm.getAvatar());

//                创建微信用户对象
                UserWechat userWechat = new UserWechat();
                userWechat.setUserId(sysUser.getSysUserId());
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

    @GetMapping("/checkToken")
    @ApiOperation(value = "检查token")
    @ApiImplicitParam(name = "wxToken", value = "验证公众号的token信息", required = true, dataType = "WxToken", paramType = "body")
    private String checkToken(WxToken wxToken){
        log.info("checkToken");
        log.info("开始验证token信息");
        log.info("开始验证wxToken"+wxToken.getSignature());
        log.info("开始验证wxToken.getEchostr"+wxToken.getEchostr());
        if (wxToken.getSignature() != null &&
                CheckoutUtil.checkSignature(wxToken.getSignature(),
                        wxToken.getTimestamp(),
                        wxToken.getNonce())) {
            return wxToken.getEchostr();
        }
        return null;
    }
    @PostMapping("/checkToken")
    @ApiOperation(value = "用户关注")
    @ApiImplicitParam(name = "wxEvent", value = "公众号关注对象", required = true, dataType = "WxEvent", paramType = "body")
    private void checkTokenRespone(WxEvent wxEvent){

        log.info("消息来源于:"+wxEvent.getFromUserName());
        log.info("openid:"+wxEvent.getToUserName());
        log.info("消息类型:"+wxEvent.getMsgType());
        String msgType = wxEvent.getMsgType();
        String eventType = wxEvent.getEvent();
        String fromUserName = wxEvent.getFromUserName();
        String toUserName = wxEvent.getToUserName();
        if(REQ_MESSAGE_TYPE_EVENT.equals(msgType)){
            //如果为事件类型
            if(EVENT_TYPE_SUBSCRIBE.equals(eventType)){
                /**
                 * 处理订阅事件
                 */
                log.info("用户："+ fromUserName +"关注~");
                //开始获取公众号用户的UnionID
                //获取access_token
                String url = TOKEN+"&appid=" +WeChatConstant.WXGZHAPPID+"&secret="+WeChatConstant.WXGZHAPPSECRET;
                String content = null;
                try {
                    content = HttpUtils.Get(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String accessToken = (String) JSON.parseObject(content).get("access_token");
                String userInfoUrl = INFOUNIONID +"?access_token="+accessToken+"&openid="+fromUserName+"&lang=zh_CN";
                String userInfoContent = null;
                try {
                    userInfoContent = HttpUtils.Get(userInfoUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = JSON.parseObject(userInfoContent);
                String unionId = (String)jsonObject.get("unionid");
                //开始插入用户数据
                //判断用户unionId是否存在
                UserWechat userWechat = iUserWechatService.selectByUserWeChat(new UserWechat().setUnionId(unionId));
                if (null!=userWechat){
//                    当前用户已存在，判断是否存在公众号的openid
                    if (StringUtils.isBlank(userWechat.getGongzhonghaoOpenid())){
//                        开始更新微信用户表数据
                        userWechat.setGongzhonghaoOpenid(fromUserName);
                        iUserWechatService.updateById(userWechat);
                    }
//                    判断用户表是否存在
                }else {
                    //开始插入微信用户数据
                    UserWechat userWech = new UserWechat();
                    String id = String.valueOf(generateId());
                    userWech.setId(id);
                    userWech.setNickname(jsonObject.getString("nickname"));
                    userWech.setSex(jsonObject.getInteger("sex"));
                    userWech.setGongzhonghaoOpenid(jsonObject.getString("openid"));
                    userWech.setUnionId(jsonObject.getString("unionid"));
                    userWech.setCity(jsonObject.getString("city"));
                    userWech.setCountry(jsonObject.getString("country"));
                    userWech.setProvince(jsonObject.getString("province"));
                    userWech.setUpdatedAt(new Date());
                    userWech.setHeadimgurl(jsonObject.getString("headimgurl"));
                    userWech.setCreatedAt(new Date());
                    userWech.setGroupid(jsonObject.getInteger("groupid"));
                    userWech.setLanguage(jsonObject.getString("language"));
                    userWech.setQrScene(jsonObject.getString("qr_scene"));
                    userWech.setQrSceneStr(jsonObject.getString("qr_scene_str"));
                    userWech.setSubscribe(jsonObject.getInteger("subscribe"));
                    iUserWechatService.save(userWech);
                }
            }else if(EVENT_TYPE_UNSUBSCRIBE.equals(eventType)){
                /**
                 * 处理取消订阅事件
                 */
                log.info("用户："+ fromUserName +"取消关注~");
            }
        }
    }

}
