package com.ryuntech.saas.api.service;

import com.ryuntech.saas.api.dto.IndexDTO;
import com.ryuntech.saas.api.dto.WeChatIndexDTO;
import com.ryuntech.saas.api.form.IndexDataBriefingForm;
import com.ryuntech.saas.api.form.WeChatIndexDetailForm;
import com.ryuntech.saas.api.model.Index;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;

import java.util.List;
import java.util.Map;

/**
 *
 * @author EDZ
 */
public interface IndexService extends IBaseService<Index> {

    /**
     * 数据简报本月回款
     */
    String queryBackMoney(IndexDataBriefingForm indexDataBriefingForm);

    /**
     * 本月销售额和应收未回款
     */
    Map<String, Object> queryAmountMoney(IndexDataBriefingForm indexDataBriefingForm);

    /**
     * 根据部门集合查询该合同编号集合
     * @param departmentNameList
     * @return
     */
    List<String> queryContractIdList(List<String> departmentNameList);

    /**
     * 数据简报统计
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
     * 指定时间内计划的列表
     * @param weChatIndexDTO
     * @return
     */
    List<ReceivableCollectionPlan> selectExpireList(WeChatIndexDTO weChatIndexDTO);

    /**
     * 指定时间内计划的金额
     * @param weChatIndexDTO
     * @return
     */
    String selectExpireSum(WeChatIndexDTO weChatIndexDTO);

    /**
     * 合同总数
     * @param weChatIndexDetailForm
     * @return
     */
    Integer totalContractNumber(WeChatIndexDetailForm weChatIndexDetailForm);

    /**
     * 客户总数
     * @param weChatIndexDetailForm
     * @return
     */
    Integer totalCustomerNumber(WeChatIndexDetailForm weChatIndexDetailForm);


    /**
     * 合同总数
     * @param weChatIndexDetailForm
     * @return
     */
    List<ReceivableContract> totalReceivableSum(WeChatIndexDetailForm weChatIndexDetailForm);

}
