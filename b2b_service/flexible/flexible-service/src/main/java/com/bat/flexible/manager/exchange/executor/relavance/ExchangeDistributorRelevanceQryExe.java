package com.bat.flexible.manager.exchange.executor.relavance;

import com.bat.flexible.dao.exchange.ExchangeDistributorRelevanceDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeDistributorRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeDistributorRelevanceQryExe {

    @Autowired
    private ExchangeDistributorRelevanceDOMapper exchangeDistributorRelevanceDOMapper;

    public List<ExchangeDistributorRelevanceDO> listByExchangeIdAndDistributorId(Integer exchangeId,Integer distributorId) {
        return exchangeDistributorRelevanceDOMapper.listByExchangeIdAndDistributorId(exchangeId,distributorId);
    }
}
