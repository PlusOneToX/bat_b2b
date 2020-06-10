package com.bat.flexible.manager.exchange.executor.relavance;

import com.bat.flexible.dao.exchange.ExchangeMaterialRelevanceDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeMaterialRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeMaterialRelevanceQryExe {

    @Autowired
    private ExchangeMaterialRelevanceDOMapper exchangeMaterialRelevanceDOMapper;


    public List<ExchangeMaterialRelevanceDO> listByExchangeId(Integer exchangeId) {
        return exchangeMaterialRelevanceDOMapper.listByExchangeId(exchangeId);
    }

    public ExchangeMaterialRelevanceDO findByExchangeIdAndMaterialId(Integer exchangeId, Integer materialId) {
        return exchangeMaterialRelevanceDOMapper.findByExchangeIdAndMaterialId(exchangeId,materialId);
    }
}
