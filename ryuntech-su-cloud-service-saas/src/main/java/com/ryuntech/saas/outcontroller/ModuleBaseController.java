package com.ryuntech.saas.outcontroller;

import com.ryuntech.common.constant.generator.IncrementIdGenerator;
import com.ryuntech.common.constant.generator.UniqueIdGenerator;
import com.ryuntech.common.controller.BaseController;
import com.ryuntech.saas.api.helper.SecurityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author EDZ
 */
public class ModuleBaseController extends BaseController {

    protected long generateId() {
        return UniqueIdGenerator.getInstance(IncrementIdGenerator.getServiceId()).nextId();
    }




    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    protected  HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected String getUserName(){
        return SecurityUtils.getUsername();
    }

}
