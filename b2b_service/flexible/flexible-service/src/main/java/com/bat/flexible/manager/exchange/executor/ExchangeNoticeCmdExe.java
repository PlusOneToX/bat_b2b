package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeNoticeDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeNoticeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeNoticeCmdExe {

    @Autowired
    private ExchangeNoticeDOMapper exchangeNoticeDOMapper;

    public void create(ExchangeNoticeDO exchangeNotice) {
        exchangeNoticeDOMapper.insert(exchangeNotice);
    }

    public void update(ExchangeNoticeDO exchangeNotice) {
        exchangeNoticeDOMapper.updateByPrimaryKey(exchangeNotice);
    }

    public void deleteById(Integer id) {
        exchangeNoticeDOMapper.deleteByPrimaryKey(id);
    }
}
