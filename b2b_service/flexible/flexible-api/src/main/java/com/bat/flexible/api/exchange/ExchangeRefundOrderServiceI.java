package com.bat.flexible.api.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeRefundOrderDO;

public interface ExchangeRefundOrderServiceI {
    /**
     * 根据退款单号查询退款单
     * @param refundNo
     * @return
     */
    ExchangeRefundOrderDO getByRefundNo(String refundNo);

    /**
     * 新增退款单
     * @param exchangeRefundOrder
     */
    void create(ExchangeRefundOrderDO exchangeRefundOrder);
}
