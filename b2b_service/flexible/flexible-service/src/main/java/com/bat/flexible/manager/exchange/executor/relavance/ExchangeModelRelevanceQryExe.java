package com.bat.flexible.manager.exchange.executor.relavance;

import com.bat.flexible.dao.exchange.ExchangeModelRelevanceDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeModelRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeModelRelevanceQryExe {

    @Autowired
    private ExchangeModelRelevanceDOMapper exchangeModelRelevanceDOMapper;


    public List<ExchangeModelRelevanceDO> listByExchangeId(Integer exchangeId) {
        return exchangeModelRelevanceDOMapper.listByExchangeId(exchangeId);
    }

    public ExchangeModelRelevanceDO findOneByExchangeIdAndModelId(Integer exchangeId, Integer modelId) {
        return exchangeModelRelevanceDOMapper.findOneByExchangeIdAndModelId(exchangeId,modelId);
    }
}
