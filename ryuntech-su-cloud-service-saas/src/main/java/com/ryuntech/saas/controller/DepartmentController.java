package com.ryuntech.saas.controller;

import com.ryuntech.saas.api.service.IDepartmentService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/department")
@Api(value = "DepartmentController", tags = {"部门管理"})
public class DepartmentController extends ModuleBaseController {

    @Autowired
    private IDepartmentService iDepartmentService;

}
