package com.bat.flexible.manager.exchange.executor.code;

import com.bat.flexible.manager.exchange.executor.ExchangeCodeQryExe;
import com.bat.flexible.dao.exchange.ExchangeCodeSyncBackLogDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeSyncBackLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeCodeSyncBackLogQryExe {

    @Autowired
    private ExchangeCodeSyncBackLogDOMapper exchangeCodeSyncBackLogDOMapper;

    @Autowired
    private ExchangeCodeQryExe exchangeCodeQryExe;

    public ExchangeCodeSyncBackLogDO findOneByBoxCodeAndStatus(String boxCode, Short status) {
        return exchangeCodeSyncBackLogDOMapper.findOneByBoxCodeAndStatus(boxCode,status);
    }

    public List<ExchangeCodeSyncBackLogDO> listUnUseBoxCodeNumBetween(Integer start, Integer end, Integer itemId) {
        return exchangeCodeSyncBackLogDOMapper.listUnUseBoxCodeNumBetween(start,end,itemId);
    }

    public ExchangeCodeSyncBackLogDO countByByItemId(Integer itemId) {
        ExchangeCodeSyncBackLogDO backLog = exchangeCodeSyncBackLogDOMapper.findLastByItemId(itemId);
        if(backLog !=null){
            return backLog;
        }
        backLog = new ExchangeCodeSyncBackLogDO();
        List<ExchangeCodeDO> codeList = exchangeCodeQryExe.countByBoxCode(itemId);
        if(codeList !=null && codeList.size()>0){
            Integer size = codeList.size();
            backLog.setAlreadySyncBoxCount(size);
        }else{
            backLog.setAlreadySyncPlainCodeCount(0);
        }
        List<ExchangeCodeDO> plainCodeList = exchangeCodeQryExe.countByBoxCodeIsNotNull(itemId);
        if(plainCodeList !=null && plainCodeList.size()>0){
            Integer size = plainCodeList.size();
            backLog.setAlreadySyncPlainCodeCount(size);
        }else{
            backLog.setAlreadySyncPlainCodeCount(0);
        }
        return backLog;
    }
}
