package com.bat.flexible.manager.exchange.executor.relavance;

import com.bat.flexible.dao.exchange.ExchangePictureRelevanceDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangePictureRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangePictureRelevanceQryExe {

    @Autowired
    private ExchangePictureRelevanceDOMapper exchangePictureRelevanceDOMapper;

    public List<ExchangePictureRelevanceDO> listByExchangeId(Integer exchangeId) {
        return exchangePictureRelevanceDOMapper.listByExchangeId(exchangeId);
    }

    public ExchangePictureRelevanceDO findOneByExchangeId(Integer exchangeId) {
        return exchangePictureRelevanceDOMapper.findOneByExchangeId(exchangeId);
    }

    public ExchangePictureRelevanceDO findByExchangeIdAndPictureId(Integer exchangeId, Integer pictureId) {
        return exchangePictureRelevanceDOMapper.findByExchangeIdAndPictureId(exchangeId,pictureId);
    }
}
