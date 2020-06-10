package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeFactoryDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeFactoryDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeFactoryQryExe {

    @Autowired
    private ExchangeFactoryDOMapper exchangeFactoryDOMapper;

    public ExchangeFactoryDO getById(Integer exchangeFactoryId) {
        return exchangeFactoryDOMapper.selectByPrimaryKey(exchangeFactoryId);
    }
}
