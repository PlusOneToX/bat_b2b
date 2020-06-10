package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeFactoryDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeFactoryDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeFactoryCmdExe {

    @Autowired
    private ExchangeFactoryDOMapper exchangeFactoryDOMapper;

    public void update(ExchangeFactoryDO exchangeFactoryDO) {
        exchangeFactoryDOMapper.updateByPrimaryKey(exchangeFactoryDO);
    }

    public void create(ExchangeFactoryDO factory) {
        exchangeFactoryDOMapper.insert(factory);
    }
}
