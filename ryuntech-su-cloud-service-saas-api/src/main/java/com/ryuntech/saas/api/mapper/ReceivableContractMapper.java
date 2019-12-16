package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.dto.ReceiveBalanceContractAmount;
import com.ryuntech.saas.api.dto.ReceiveBalanceRepayAmount;
import com.ryuntech.saas.api.model.ReceivableContract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 应收合同表 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-09-27
 */
@Component
public interface ReceivableContractMapper extends IBaseMapper<ReceivableContract> {
    /**
     * 分页查询合同
     *
     * @param page
     * @param receivableContract
     * @return
     */
    IPage<ReceivableContract> selectPageList(@Param("pg") Page<ReceivableContract> page, @Param("receivableContract") ReceivableContract receivableContract);

    /**
     * @param receivableContractDTO
     * @return
     */
    String selectSumByRContract(@Param("receivableContractDTO") ReceivableContractDTO receivableContractDTO);

    // 查询部门集合（部门为主）指定时间内的新增销售额
    String getDepartmentsContractAmounts(@Param("departmentIds") List<String> departmentIds, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    // 查询部门集合（部门为主）指定时间内的新增回款
    String getDepartmentsRepayAmounts(@Param("departmentIds") List<String> departmentIds, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    // 查部门集合（员工为主）指定时间内的新增销售额
    List<ReceiveBalanceContractAmount> getEmployeesContractAmounts(@Param("departmentIds") List<String> departmentIds, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    // 查询部门集合（员工为主）指定时间内的新增回款
    List<ReceiveBalanceRepayAmount> getEmployeesRepayAmounts(@Param("departmentIds") List<String> departmentIds, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    // 查询部门集合（客户为主）指定时间内的新增销售额
    List<ReceiveBalanceContractAmount> getCustomersContractAmounts(@Param("departmentIds") List<String> departmentIds, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    // 查询部门集合（客户为主）指定时间内的新增回款
    List<ReceiveBalanceRepayAmount> getCustomersRepayAmounts(@Param("departmentIds") List<String> departmentIds, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
}
