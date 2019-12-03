package com.ryuntech.saas.controller;

import com.ryuntech.saas.api.service.IEmployeeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/employee")
@Api(value = "EmployeeController", tags = {"员工帐号管理"})
public class EmployeeController extends ModuleBaseController {

    @Autowired
    private IEmployeeService iEmployeeService;

}
