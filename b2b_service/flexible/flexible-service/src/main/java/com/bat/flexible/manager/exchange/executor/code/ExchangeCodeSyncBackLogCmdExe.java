package com.bat.flexible.manager.exchange.executor.code;

import com.bat.flexible.dao.exchange.ExchangeCodeSyncBackLogDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeSyncBackLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeCodeSyncBackLogCmdExe {

    @Autowired
    private ExchangeCodeSyncBackLogDOMapper exchangeCodeSyncBackLogDOMapper;


    public void createList(List<ExchangeCodeSyncBackLogDO> exchangeCodeSyncBackLogDOList) {
        exchangeCodeSyncBackLogDOMapper.batchInsert(exchangeCodeSyncBackLogDOList);
    }

    public void create(ExchangeCodeSyncBackLogDO exchangeCodeSyncBackLogDO) {
        exchangeCodeSyncBackLogDOMapper.insert(exchangeCodeSyncBackLogDO);
    }
}
