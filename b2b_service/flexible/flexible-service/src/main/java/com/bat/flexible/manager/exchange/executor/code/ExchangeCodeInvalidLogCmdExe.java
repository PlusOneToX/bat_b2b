package com.bat.flexible.manager.exchange.executor.code;

import com.bat.flexible.dao.exchange.ExchangeCodeInvalidLogDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeInvalidLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCodeInvalidLogCmdExe {

    @Autowired
    private ExchangeCodeInvalidLogDOMapper exchangeCodeInvalidLogDOMapper;


    public void create(ExchangeCodeInvalidLogDO invalidLog) {
        exchangeCodeInvalidLogDOMapper.insert(invalidLog);
    }
}
