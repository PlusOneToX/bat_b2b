package com.bat.flexible.manager.exchange.executor.code;

import com.bat.flexible.dao.exchange.ExchangeCodeOutboundRestoreLogDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeOutboundRestoreLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCodeOutboundRestoreLogCmdExe {

    @Autowired
    private ExchangeCodeOutboundRestoreLogDOMapper exchangeCodeOutboundRestoreLogDOMapper;


    public void create(ExchangeCodeOutboundRestoreLogDO restoreLog) {
        exchangeCodeOutboundRestoreLogDOMapper.insert(restoreLog);
    }
}
