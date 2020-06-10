package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeGeneralItemPoolDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeGeneralItemPoolDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeGeneralItemPoolQryExe {

    @Autowired
    private ExchangeGeneralItemPoolDOMapper exchangeGeneralItemPoolDOMapper;

    /**
     * 模糊查询80码
     * @param content
     * @param openFlag
     * @return
     */
    public List<ExchangeGeneralItemPoolDO> listByItemCodeDimAndOpenFlag(String content, Short openFlag) {

        return exchangeGeneralItemPoolDOMapper.listByItemCodeDimAndOpenFlag(content,openFlag);
    }
}
