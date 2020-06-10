package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeCardTransferMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardTransferDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCardTransferQryExe {

    @Autowired
    private ExchangeCardTransferMapper exchangeCardTransferMapper;

    public ExchangeCardTransferDO selectByExchangeId(Integer exchangeId){
       return exchangeCardTransferMapper.selectByExchangeId(exchangeId);
    }
}
