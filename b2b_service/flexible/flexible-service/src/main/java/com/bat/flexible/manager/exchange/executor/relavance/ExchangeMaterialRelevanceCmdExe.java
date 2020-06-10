package com.bat.flexible.manager.exchange.executor.relavance;

import com.bat.flexible.dao.exchange.ExchangeMaterialRelevanceDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeMaterialRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeMaterialRelevanceCmdExe {

    @Autowired
    private ExchangeMaterialRelevanceDOMapper exchangeMaterialRelevanceDOMapper;


    public void create(ExchangeMaterialRelevanceDO relevanceDO) {
        exchangeMaterialRelevanceDOMapper.insert(relevanceDO);
    }

    public void deleteById(Integer id) {
        exchangeMaterialRelevanceDOMapper.deleteByPrimaryKey(id);
    }
}
