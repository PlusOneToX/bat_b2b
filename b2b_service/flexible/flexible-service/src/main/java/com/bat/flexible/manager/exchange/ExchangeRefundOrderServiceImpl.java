package com.bat.flexible.manager.exchange;

import com.bat.flexible.manager.exchange.executor.ExchangeRefundOrderCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeRefundOrderQryExe;
import com.bat.flexible.api.exchange.ExchangeRefundOrderServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangeRefundOrderDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExchangeRefundOrderServiceImpl implements ExchangeRefundOrderServiceI {

    @Autowired
    private ExchangeRefundOrderCmdExe exchangeRefundOrderCmdExe;

    @Autowired
    private ExchangeRefundOrderQryExe exchangeRefundOrderQryExe;

    @Override
    public ExchangeRefundOrderDO getByRefundNo(String refundNo) {
        return exchangeRefundOrderQryExe.getByRefundNo(refundNo);
    }

    @Transactional
    @Override
    public void create(ExchangeRefundOrderDO exchangeRefundOrder) {
        exchangeRefundOrderCmdExe.create(exchangeRefundOrder);
    }
}
