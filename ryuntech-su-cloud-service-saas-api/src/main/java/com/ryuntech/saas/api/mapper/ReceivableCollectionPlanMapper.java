package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.*;
import com.ryuntech.saas.api.form.ReceivableCollectionPlanForm;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-09-29
 */
@Component
public interface ReceivableCollectionPlanMapper extends IBaseMapper<ReceivableCollectionPlan> {

    /**
     * 分页查询
     *
     * @param page
     * @param receivableCollectionPlan
     * @return
     */
    IPage<ReceivableCollectionPlan> selectPageList(@Param("pg") Page<ReceivableCollectionPlan> page, @Param("receivableCollectionPlan") ReceivableCollectionPlan receivableCollectionPlan);

    /**
     * 批量插入计划
     *
     * @param receivableCollectionPlans
     * @return
     */
    int insertBatch(List<ReceivableCollectionPlan> receivableCollectionPlans);

    /**
     * 查询合同对应的计划
     *
     * @param receivableCollectionPlans
     * @return
     */
    List<ReceivableCollectionPlan> selectByPlan(@Param("plan") ReceivableCollectionPlanForm receivableCollectionPlans);

    List<RepayPlanByContractDTO> repayPlanByContractList(@Param("departmentIds") List<String> departmentIds, @Param("status") String status, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<RepayPlanByDepartmentDTO> repayPlanByDepartmentList(@Param("departmentIds") List<String> departmentIds, @Param("status") String status, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<RepayPlanByCustomerDTO> repayPlanByCustomerList(@Param("departmentIds") List<String> departmentIds, @Param("status") String status, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<RepayPlanByEmployeeDTO> repayPlanByEmployeeList(@Param("departmentIds") List<String> departmentIds, @Param("status") String status, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<RepayPlanByCustomerDetailDTO> repayPlanByCustomerDetail(@Param("departmentIds") List<String> departmentIds, @Param("status") String status, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("customerId") String customerId);

    List<RepayPlanByEmployeeDetailDTO> repayPlanByEmployeeDetail(@Param("departmentIds") List<String> departmentIds, @Param("status") String status, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("employeeId") String employeeId);

    List<OverdueByContractDTO> overdueByContractList(@Param("departmentIds") List<String> departmentIds);

    List<OverdueByCustomerDTO> overdueByCustomerList(@Param("departmentIds") List<String> currentUserDepartments);

    List<OverdueByEmployeeDTO> overdueByEmployeeList(@Param("departmentIds") List<String> currentUserDepartments);

    /**
     * 轮询逾期计划
     * @param receivableCollectionPlanForm
     * @return
     */
    List<ReceivableCollectionPlan> selectOverdueRemind(ReceivableCollectionPlanForm receivableCollectionPlanForm);
}
