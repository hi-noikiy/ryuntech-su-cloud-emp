package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.saas.api.model.SysParams;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author antu
 * @since 2019-08-15
 */
public interface ISysParamsService extends IService<SysParams> {

    /**
     * 根据innerName获取字段表数据
     * @param innerName
     * @return
     */
    String getValueByInnerName(String innerName);

    /**
     * 根据outerName获取字段表数据
     * @param outerName
     * @return
     */
    String getValueByOuterName(String outerName);
}
