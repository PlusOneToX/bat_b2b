package com.bat.flexible.manager.exchange.executor.code;

import com.bat.flexible.dao.exchange.ExchangeCodeRestoreLogDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeRestoreLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCodeRestoreLogCmdExe {

    @Autowired
    private ExchangeCodeRestoreLogDOMapper exchangeCodeRestoreLogDOMapper;


    public void create(ExchangeCodeRestoreLogDO exchangeCodeRestoreLog) {
        exchangeCodeRestoreLogDOMapper.insert(exchangeCodeRestoreLog);
    }
}
