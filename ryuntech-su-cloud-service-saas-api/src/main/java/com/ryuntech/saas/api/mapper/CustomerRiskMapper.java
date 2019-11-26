package com.ryuntech.saas.api.mapper;

import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.CustomerRiskDTO;
import com.ryuntech.saas.api.dto.IndexDTO;
import com.ryuntech.saas.api.form.CustomerRiskForm;
import com.ryuntech.saas.api.model.CustomerRisk;
import com.ryuntech.saas.api.model.Index;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户风险表 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-11-06
 */
public interface CustomerRiskMapper extends IBaseMapper<CustomerRisk> {


    /**
     * 查询列表
     * @param customerRiskForm
     * @return
     */
    List<CustomerRiskDTO> selectGroupConcat(@Param("customerRiskForm") CustomerRiskForm customerRiskForm);


    /**
     * 根据时间查询列表
     * @param customerRiskForm
     * @return
     */
    List<CustomerRiskDTO> selectGroupConcatByTime(@Param("customerRiskForm") CustomerRiskForm customerRiskForm);

}
