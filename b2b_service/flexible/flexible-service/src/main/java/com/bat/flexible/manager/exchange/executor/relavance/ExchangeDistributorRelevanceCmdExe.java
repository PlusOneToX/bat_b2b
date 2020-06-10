package com.bat.flexible.manager.exchange.executor.relavance;

import com.bat.flexible.dao.exchange.ExchangeDistributorRelevanceDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeDistributorRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeDistributorRelevanceCmdExe {

    @Autowired
    private ExchangeDistributorRelevanceDOMapper exchangeDistributorRelevanceDOMapper;

    public void delete(Integer id) {
        exchangeDistributorRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void create(ExchangeDistributorRelevanceDO distributorRelevanceDO) {
        exchangeDistributorRelevanceDOMapper.insert(distributorRelevanceDO);
    }
}
