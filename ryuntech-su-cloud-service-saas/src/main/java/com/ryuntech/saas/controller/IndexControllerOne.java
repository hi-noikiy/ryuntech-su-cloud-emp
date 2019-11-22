package com.ryuntech.saas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.IndexDTO;
import com.ryuntech.saas.api.dto.IndexDataBriefingDTO;
import com.ryuntech.saas.api.dto.IndexDepartmentDTO;
import com.ryuntech.saas.api.form.IndexDataBriefingForm;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private IEmployeeService iEmployeeService;

    @Autowired
    private IDepartmentService iDepartmentService;

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

    @PostMapping("/dataBriefing")
    @ApiOperation(value = "首页数据简报展示")
    @ApiImplicitParam(name = "indexDataBriefingForm", value = "数据简报请求参数", required = true, dataType = "IndexDataBriefingForm", paramType = "body")
    public Result<IndexDataBriefingDTO> indexDataBriefing(@RequestBody IndexDataBriefingForm indexDataBriefingForm) {
        List<String> departmentNameList = indexDataBriefingForm.getDepartmentNames();
        List<String> contractIdList = indexService.queryContractIdList(departmentNameList);
        if(contractIdList != null) {
            indexDataBriefingForm.setContractIds(contractIdList);
        }
        String backMoney = indexService.queryBackMoney(indexDataBriefingForm);
        IndexDataBriefingDTO indexDataBriefingDTO = new IndexDataBriefingDTO();
        indexDataBriefingDTO.setBackMoney(backMoney);
        Map<String, Object> amountMoneyMap = indexService.queryAmountMoney(indexDataBriefingForm);
        if(amountMoneyMap != null) {
            for(String amm : amountMoneyMap.keySet()) {
                if("noRepayment".equals(amm)) {
                    indexDataBriefingDTO.setNoRepayment(String.valueOf(amountMoneyMap.get(amm)));
                }
                if("salesVolume".equals(amm)) {
                    indexDataBriefingDTO.setSalesVolume(String.valueOf(amountMoneyMap.get(amm)));
                }
            }
        }
        return new Result(indexDataBriefingDTO);
    }

    /**
     * 首页选择部门及层级关系
     *
     * @return
     */
    @GetMapping("/departrelation")
    @ApiOperation(value = "首页选择部门及层级关系")
    public Result<IndexDepartmentDTO> departmentSelect() {
        String username = getUserName();
        SysUser user = sysUserService.findByName(username);
        //当前用户所属公司职工编号
        String employeeId = user.getEmployeeId();
        IndexDepartmentDTO indexDepartmentDTO = new IndexDepartmentDTO();
        Employee employee = iEmployeeService.getById("749875506521833471");
        String departmentId = null;
        if(employee != null) {
            departmentId = employee.getDepartmentId();
            indexDepartmentDTO.setDepartmentId(departmentId);
            indexDepartmentDTO.setDepartmentName(employee.getDepartmentName());
        }
        List<IndexDepartmentDTO> list = new ArrayList<>();
        list = getBack(departmentId);
        indexDepartmentDTO.setSonDepartment(list);
        return new Result(indexDepartmentDTO);
    }

    // 递归遍历部门层级关系
    public List<IndexDepartmentDTO> getBack(String departmentId) {
        List<Department> departmentList = iDepartmentService.list(new QueryWrapper<Department>().eq("pid",departmentId));
        List<IndexDepartmentDTO> indexDepartmentDTOList = new ArrayList<>();
        if(departmentList != null) {
            for(Department department : departmentList) {
                IndexDepartmentDTO indexDepartmentDTO = new IndexDepartmentDTO();
                indexDepartmentDTO.setDepartmentId(department.getDepartmentId());
                indexDepartmentDTO.setDepartmentName(department.getDepartmentName());
                indexDepartmentDTO.setSonDepartment(getBack(department.getDepartmentId()));
                indexDepartmentDTOList.add(indexDepartmentDTO);
            }
        } else {
            return null;
        }
        return indexDepartmentDTOList;
    }

}
