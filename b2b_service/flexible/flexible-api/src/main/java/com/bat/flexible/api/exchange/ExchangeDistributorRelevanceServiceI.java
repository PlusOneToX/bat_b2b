package com.bat.flexible.api.exchange;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.dao.exchange.dataobject.ExchangeDistributorRelevanceDO;

import java.util.List;

public interface ExchangeDistributorRelevanceServiceI {

    /**
     * 设置关联关系
     * @param isAdd 是否新增
     * @param distributorScope 兑换卡的分销商范围
     * @param distributorList 分销商关联关系
     * @param exchangeId 兑换卡活动id
     */
    void saveRelevance(boolean isAdd, Short distributorScope, List<DistributorSimpleRelaQry> distributorList, Integer exchangeId
            , AdminResponse currentAdmin);

    /**
     * 根据兑换卡id或者分销商id查询兑换卡分销商关联列表
     * @param exchangeId
     * @param distributorId
     * @return
     */
    List<ExchangeDistributorRelevanceDO> listByExchangeIdAndDistributorId(Integer exchangeId, Integer distributorId);
}
