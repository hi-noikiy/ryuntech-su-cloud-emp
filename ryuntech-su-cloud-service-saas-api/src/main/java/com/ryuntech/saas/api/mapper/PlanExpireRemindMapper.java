package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryuntech.saas.api.dto.ContractPlanRemindDTO;
import com.ryuntech.saas.api.model.PlanExpireRemind;

import java.util.List;

/**
 * <p>
 * 应收计划到期提醒 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-12-03
 */
public interface PlanExpireRemindMapper extends BaseMapper<PlanExpireRemind> {

    /**
     * 查询所有有还款计划且未还款相应信息
     * @return
     */
    List<ContractPlanRemindDTO> queryPlanMessageRemind();

}
