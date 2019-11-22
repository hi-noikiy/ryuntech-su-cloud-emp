package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.CustomerRiskDTO;
import com.ryuntech.saas.api.form.CustomerRiskForm;
import com.ryuntech.saas.api.model.CustomerMonitor;
import com.ryuntech.saas.api.model.CustomerRisk;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户风险表 服务类
 * </p>
 *
 * @author antu
 * @since 2019-11-06
 */
public interface ICustomerRiskService extends IBaseService<CustomerRisk> {

    /**
     * 分页查询
     * @param customerRiskForm
     * @param queryPage
     * @return
     */
    Result<IPage<CustomerRisk>> pageList(CustomerRiskForm customerRiskForm, QueryPage queryPage);

    /**
     * 查询列表
     * @param customerRiskForm
     * @return
     */
    List<CustomerRiskDTO> selectGroupConcat(@Param("customerRiskForm") CustomerRiskForm customerRiskForm);

    /**
     * 根据时间分组
     * @param customerRiskForm
     * @return
     */
    List<CustomerRiskDTO> selectGroupConcatByTime(@Param("customerRiskForm") CustomerRiskForm customerRiskForm);
}
