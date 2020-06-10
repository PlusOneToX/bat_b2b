package com.bat.flexible.manager.exchange.executor.code;

import com.bat.flexible.dao.exchange.ExchangeCodeUserDOMapper;
import com.bat.flexible.dao.exchange.co.ExchangeCodeUserCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeUserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeCodeUserQryExe {

    @Autowired
    private ExchangeCodeUserDOMapper exchangeCodeUserDOMapper;



    public List<ExchangeCodeUserCO> listCOByUserIdAndStatus(Integer userId, Short status) {
        return exchangeCodeUserDOMapper.listCOByUserIdAndStatus(userId,status);
    }

    public ExchangeCodeUserCO findByExchangeCodeId(Integer id) {
        return exchangeCodeUserDOMapper.findByExchangeCodeId(id);
    }

    public ExchangeCodeUserDO getByExchangeCodeId(Integer exchangeCodeId) {
        return exchangeCodeUserDOMapper.getByExchangeCodeId(exchangeCodeId);
    }

    public Integer countByCondition(Integer userId, Short status,Integer materialId) {
        return exchangeCodeUserDOMapper.countByCondition(userId,status,materialId);
    }


}
