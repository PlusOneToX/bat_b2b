package com.bat.flexible.manager.exchange.executor.relavance;

import com.bat.flexible.dao.exchange.ExchangeModelRelevanceDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeModelRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeModelRelevanceCmdExe {

    @Autowired
    private ExchangeModelRelevanceDOMapper exchangeModelRelevanceDOMapper;


    public void create(ExchangeModelRelevanceDO relevanceDO) {
        exchangeModelRelevanceDOMapper.insert(relevanceDO);
    }

    public void deleteById(Integer id) {
        exchangeModelRelevanceDOMapper.deleteByPrimaryKey(id);
    }
}
