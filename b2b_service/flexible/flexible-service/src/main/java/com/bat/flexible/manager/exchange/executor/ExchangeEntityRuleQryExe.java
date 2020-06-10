package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeEntityRuleDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeEntityRuleDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeEntityRuleQryExe {

    @Autowired
    private ExchangeEntityRuleDOMapper exchangeEntityRuleDOMapper;

    public ExchangeEntityRuleDO getByExchangeId(Integer exchangeId) {
        return exchangeEntityRuleDOMapper.getByExchangeId(exchangeId);
    }
}
