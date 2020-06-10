package com.bat.flexible.api.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeFactoryDO;

public interface ExchangeFactoryServiceI {
    ExchangeFactoryDO getById(Integer exchangeFactoryId);

    void update(ExchangeFactoryDO exchangeFactoryDO);

    void create(ExchangeFactoryDO factory);
}
