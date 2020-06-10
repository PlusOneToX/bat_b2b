package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.exchange.ExchangeNoticeDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeNoticeDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeNoticeQryExe {

    @Autowired
    private ExchangeNoticeDOMapper exchangeNoticeDOMapper;

    public List<ExchangeNoticeDO> listAllDesc() {
        return exchangeNoticeDOMapper.listAllDesc();
    }

    public ExchangeNoticeDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        ExchangeNoticeDO exchangeNoticeDO = exchangeNoticeDOMapper.selectByPrimaryKey(id);
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return exchangeNoticeDO;
    }
}
