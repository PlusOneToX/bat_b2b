package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeEntityRuleDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeEntityRuleDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeEntityRuleCmdExe {

    @Autowired
    private ExchangeEntityRuleDOMapper exchangeEntityRuleDOMapper;

    public void create(ExchangeEntityRuleDO rule) {
        exchangeEntityRuleDOMapper.insert(rule);
    }

    public void update(ExchangeEntityRuleDO rule) {
        exchangeEntityRuleDOMapper.updateByPrimaryKey(rule);
    }
}
