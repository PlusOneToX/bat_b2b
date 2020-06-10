package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeCardTransferMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardTransferDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCardTransferCmdExe {

    @Autowired
    private ExchangeCardTransferMapper exchangeCardTransferMapper;

    public void insert(ExchangeCardTransferDO exchangeCardTransferDO) {
        exchangeCardTransferMapper.insert(exchangeCardTransferDO);
    }

    public void update(ExchangeCardTransferDO exchangeCardTransferDO) {
        exchangeCardTransferMapper.updateByPrimaryKey(exchangeCardTransferDO);
    }
}
