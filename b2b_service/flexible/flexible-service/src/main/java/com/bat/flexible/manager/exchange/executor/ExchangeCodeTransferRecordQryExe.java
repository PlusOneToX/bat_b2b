package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeCodeTransferRecordMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeTransferRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCodeTransferRecordQryExe {

    @Autowired
    private ExchangeCodeTransferRecordMapper exchangeCodeTransferRecordMapper;

    public ExchangeCodeTransferRecordDO selectByCondition(Integer exchangeCodeId, Integer fromUserId, Short receiveFlag) {
        return exchangeCodeTransferRecordMapper.selectByCondition(exchangeCodeId, fromUserId, receiveFlag);
    }

    public ExchangeCodeTransferRecordDO selectById(Integer id) {
        ExchangeCodeTransferRecordDO exchangeCodeTransferRecordDO = exchangeCodeTransferRecordMapper.selectByPrimaryKey(id);
        return exchangeCodeTransferRecordDO;
    }
}
