package com.bat.thirdparty.alibaba.taobao.api;

import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeDO;

public interface TaoBaoTradeServiceI {

    TaobaoTradeDO getById(Long tid);

    void update(TaobaoTradeDO taobaoTradeDO);

    /**
     * 订单已下单
     * @param orderNo
     */
    void hasDown(String orderNo);
}
