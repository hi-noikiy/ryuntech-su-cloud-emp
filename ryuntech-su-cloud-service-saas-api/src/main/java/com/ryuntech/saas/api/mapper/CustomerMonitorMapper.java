package com.ryuntech.saas.api.mapper;

import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.form.CustomerMonitorForm;
import com.ryuntech.saas.api.form.CustomerRiskForm;
import com.ryuntech.saas.api.model.CustomerMonitor;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 客户风险监控表 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-11-20
 */
public interface CustomerMonitorMapper extends IBaseMapper<CustomerMonitor> {

    /**
     * 查询待监控数据
     * @param customerMonitorForm
     * @return
     */
    List<HashMap<String,String>> selectGroupMonitorByStaffId(@Param("customerMonitorForm") CustomerMonitorForm customerMonitorForm);

}
