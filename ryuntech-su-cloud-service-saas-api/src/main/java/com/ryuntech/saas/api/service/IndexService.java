package com.ryuntech.saas.api.service;

import com.ryuntech.saas.api.dto.IndexDTO;
import com.ryuntech.saas.api.dto.WeChatIndexDTO;
import com.ryuntech.saas.api.model.Index;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author EDZ
 */
public interface IndexService extends IBaseService<Index> {
    /**
     * 数据简报统计
     * @param indexDTO
     * @return
     */
    Index selectBulletin( IndexDTO indexDTO);

    /**
     * 总待收金额
     * @param weChatIndexDTO
     * @return
     */
    String selectBalanceAmounts( WeChatIndexDTO weChatIndexDTO);

    /**
     * 本月新增应收
     * @param weChatIndexDTO
     * @return
     */
    String selectContractAmounts( WeChatIndexDTO weChatIndexDTO);

    /**
     * 指定日期内的回款
     * @param weChatIndexDTO
     * @return
     */
    String selectCollectionAmounts(WeChatIndexDTO weChatIndexDTO);


    /**
     * 已逾期未收款数量
     * @param weChatIndexDTO
     * @return
     */
    Integer selectOverdueNumber(WeChatIndexDTO weChatIndexDTO);

    /**
     * 已逾期未收款金额
     * @param weChatIndexDTO
     * @return
     */
    String selectOverdueSum(WeChatIndexDTO weChatIndexDTO);

    /**
     * 指定时间内计划的数目
     * @param weChatIndexDTO
     * @return
     */
    Integer selectExpireNumber(WeChatIndexDTO weChatIndexDTO);

    /**
     * 指定时间内计划的金额
     * @param weChatIndexDTO
     * @return
     */
    String selectExpireSum(WeChatIndexDTO weChatIndexDTO);



}
