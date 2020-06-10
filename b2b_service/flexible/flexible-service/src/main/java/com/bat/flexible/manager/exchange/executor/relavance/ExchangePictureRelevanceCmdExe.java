package com.bat.flexible.manager.exchange.executor.relavance;

import com.bat.flexible.dao.exchange.ExchangePictureRelevanceDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangePictureRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangePictureRelevanceCmdExe {

    @Autowired
    private ExchangePictureRelevanceDOMapper exchangePictureRelevanceDOMapper;

    public void deleteById(Integer id) {
        exchangePictureRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void create(ExchangePictureRelevanceDO relevanceDO) {
        exchangePictureRelevanceDOMapper.insert(relevanceDO);
    }
}
