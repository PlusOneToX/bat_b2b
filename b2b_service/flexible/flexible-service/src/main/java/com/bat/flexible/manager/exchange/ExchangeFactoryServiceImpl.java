package com.bat.flexible.manager.exchange;

import com.bat.flexible.manager.exchange.executor.ExchangeFactoryCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeFactoryQryExe;
import com.bat.flexible.api.exchange.ExchangeFactoryServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangeFactoryDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExchangeFactoryServiceImpl implements ExchangeFactoryServiceI {

    @Autowired
    private ExchangeFactoryQryExe exchangeFactoryQryExe;

    @Autowired
    private ExchangeFactoryCmdExe exchangeFactoryCmdExe;

    @Override
    public ExchangeFactoryDO getById(Integer exchangeFactoryId) {
        return exchangeFactoryQryExe.getById(exchangeFactoryId);
    }

    @Transactional
    @Override
    public void update(ExchangeFactoryDO exchangeFactoryDO) {
        exchangeFactoryCmdExe.update(exchangeFactoryDO);
    }

    @Transactional
    @Override
    public void create(ExchangeFactoryDO factory) {
        exchangeFactoryCmdExe.create(factory);
    }
}
