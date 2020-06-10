package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeExportMapper;
import com.bat.flexible.dao.exchange.co.ExchangeExportPageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeExportDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeExportQryExe {

    @Autowired
    private ExchangeExportMapper exchangeExportMapper;

    public List<ExchangeExportPageCO> listCOByCondition(Integer exchangeId) {
        List<ExchangeExportPageCO> list = exchangeExportMapper.listCOByCondition(exchangeId);
        return list;
    }

    public ExchangeExportDO getById(Integer id) {
        return exchangeExportMapper.selectByPrimaryKey(id);
    }
}
