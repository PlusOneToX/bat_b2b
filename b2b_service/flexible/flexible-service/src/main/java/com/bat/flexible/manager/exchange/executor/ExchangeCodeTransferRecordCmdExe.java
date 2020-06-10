package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeCodeTransferRecordMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeTransferRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCodeTransferRecordCmdExe {

    @Autowired
    private ExchangeCodeTransferRecordMapper exchangeCodeTransferRecordMapper;

    public void insert(ExchangeCodeTransferRecordDO exchangeCodeTransferRecordDO) {
        exchangeCodeTransferRecordMapper.insert(exchangeCodeTransferRecordDO);
    }

    public void update(ExchangeCodeTransferRecordDO exchangeCodeTransferRecordDO) {
        exchangeCodeTransferRecordMapper.updateByPrimaryKey(exchangeCodeTransferRecordDO);
    }
}
