package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryuntech.saas.api.dto.ContractPlanRemindDTO;
import com.ryuntech.saas.api.model.PlanOverdueRemind;

import java.util.List;

/**
 * <p>
 * 应收计划逾期跟进提醒 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-12-03
 */
public interface PlanOverdueRemindMapper extends BaseMapper<PlanOverdueRemind> {

    /**
     * 查询所有有还款计划且未还款相应信息
     * @return
     */
    List<ContractPlanRemindDTO> queryPlanMessageRemind();

    /**
     * 发送微信消息
     * @return
     */
    List<ContractPlanRemindDTO> queryPlanMessageRemindWx();
}
