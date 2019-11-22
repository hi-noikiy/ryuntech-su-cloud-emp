package com.ryuntech.saas.api.mapper;

import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.IndexDTO;
import com.ryuntech.saas.api.dto.WeChatIndexDTO;
import com.ryuntech.saas.api.form.IndexDataBriefingForm;
import com.ryuntech.saas.api.form.WeChatIndexDetailForm;
import com.ryuntech.saas.api.model.Index;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author EDZ
 */
public interface IndexMapper extends IBaseMapper<Index> {

    /**
     * 数据简报本月回款
     * @param indexDataBriefingForm
     * @return
     */
    String queryBackMoney(@Param("indexDataBriefingForm") IndexDataBriefingForm indexDataBriefingForm);

    /**
     * 本月销售额和应收未回款
     * @param indexDataBriefingForm
     * @return
     */
    Map<String, Object> queryAmountMoney(@Param("indexDataBriefingForm") IndexDataBriefingForm indexDataBriefingForm);

    /**
     * 根据部门集合查询该合同编号集合
     * @param departmentNameList
     * @return
     */
    List<String> queryContractIdList(@Param("departmentNameList") List<String> departmentNameList);

    /**
     * 数据简报统计
     * @param indexDTO
     * @return
     */
    Index selectBulletin(@Param("indexDTO") IndexDTO indexDTO);


    /**
     * 总待收金额
     * @param weChatIndexDTO
     * @return
     */
    String selectBalanceAmounts(@Param("weChatIndexDTO") WeChatIndexDTO weChatIndexDTO);

    /**
     * 本月新增应收
     * @param weChatIndexDTO
     * @return
     */
    String selectContractAmounts(@Param("weChatIndexDTO") WeChatIndexDTO weChatIndexDTO);

    /**
     * 指定日期内的回款
     * @param weChatIndexDTO
     * @return
     */
    String selectCollectionAmounts(@Param("weChatIndexDTO") WeChatIndexDTO weChatIndexDTO);


    /**
     * 已逾期未收款数量
     * @param weChatIndexDTO
     * @return
     */
    Integer selectOverdueNumber(@Param("weChatIndexDTO") WeChatIndexDTO weChatIndexDTO);

    /**
     * 已逾期未收款金额
     * @param weChatIndexDTO
     * @return
     */
    String selectOverdueSum(@Param("weChatIndexDTO") WeChatIndexDTO weChatIndexDTO);

    /**
     * 指定时间内计划的数目
     * @param weChatIndexDTO
     * @return
     */
    Integer selectExpireNumber(@Param("weChatIndexDTO") WeChatIndexDTO weChatIndexDTO);


    /**
     * 指定时间内计划列表
     * @param weChatIndexDTO
     * @return
     */
    List<ReceivableCollectionPlan> selectExpireList(@Param("weChatIndexDTO") WeChatIndexDTO weChatIndexDTO);

    /**
     * 指定时间内计划的金额
     * @param weChatIndexDTO
     * @return
     */
    String selectExpireSum(@Param("weChatIndexDTO") WeChatIndexDTO weChatIndexDTO);

    /**
     * 合同总数
     * @param weChatIndexDetailForm
     * @return
     */
    Integer totalContractNumber(@Param("weChatIndexDetailForm") WeChatIndexDetailForm weChatIndexDetailForm);

    /**
     * 客户总数
     * @param weChatIndexDetailForm
     * @return
     */
    Integer totalCustomerNumber(@Param("weChatIndexDetailForm") WeChatIndexDetailForm weChatIndexDetailForm);

    /**
     * 合同总数列表
     * @param weChatIndexDetailForm
     * @return
     */
    List<ReceivableContract> totalReceivableSum(@Param("weChatIndexDetailForm")WeChatIndexDetailForm weChatIndexDetailForm);

}
