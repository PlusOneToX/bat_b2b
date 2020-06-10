package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeCardDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCardCmdExe {

    @Autowired
    private ExchangeCardDOMapper exchangeCardDOMapper;

    public void create(ExchangeCardDO exchangeCardDO) {
        exchangeCardDOMapper.insert(exchangeCardDO);
    }

    public void update(ExchangeCardDO exchangeCardDO) {
        exchangeCardDOMapper.updateByPrimaryKey(exchangeCardDO);
    }

    public void createStartExchangeEvent(String time, Integer exchangeId) {
        exchangeCardDOMapper.createStartExchangeEvent(time,exchangeId);
    }

    public void createEndExchangeEvent(String time, Integer exchangeId) {
        exchangeCardDOMapper.createEndExchangeEvent(time,exchangeId);
    }
}
