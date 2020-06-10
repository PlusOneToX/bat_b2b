package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.api.exchange.dto.ExchangeSpecialDetailDTO;
import com.bat.flexible.dao.exchange.ExchangeSpecialDistributorMapper;
import com.bat.flexible.dao.exchange.ExchangeSpecialMapper;
import com.bat.flexible.dao.exchange.co.ExchangeSpecialDistributorCO;
import com.bat.flexible.dao.exchange.co.ExchangeSpecialPageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeSpecialDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeSpecialDistributorDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeSpecialQryExe {

    @Autowired
    private ExchangeSpecialMapper exchangeSpecialMapper;

    @Autowired
    private ExchangeSpecialDistributorMapper exchangeSpecialDistributorMapper;

    public List<ExchangeSpecialPageCO> listCOByCondition(String title, Integer distributorId,Short activityPlatform,Short type) {
        List<ExchangeSpecialPageCO> list = exchangeSpecialMapper.listCOByCondition(title, distributorId,activityPlatform,type);
        return list;

    }

    public ExchangeSpecialDetailDTO detail(Integer id) {
        ExchangeSpecialDetailDTO exchangeSpecialDetailDTO = new ExchangeSpecialDetailDTO();
        ExchangeSpecialDO exchangeSpecialDO = exchangeSpecialMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(exchangeSpecialDO, exchangeSpecialDetailDTO);
        List<ExchangeSpecialDistributorCO> exchangeSpecialDistributorCOS = exchangeSpecialDistributorMapper.listCOByCondition(id, null);
        exchangeSpecialDetailDTO.setExchangeSpecialDistributorCOS(exchangeSpecialDistributorCOS);
        return exchangeSpecialDetailDTO;
    }

    public List<ExchangeSpecialDistributorCO> listDistributorCOByCondition(Integer exchangeSpecialId, Integer distributorId) {
        List<ExchangeSpecialDistributorCO> list = exchangeSpecialDistributorMapper.listCOByCondition(exchangeSpecialId, distributorId);
        return list;
    }

    public ExchangeSpecialDistributorDO selectDistributorById(Integer exchangeSpecialDistributorId) {
        return exchangeSpecialDistributorMapper.selectByPrimaryKey(exchangeSpecialDistributorId);
    }
}
