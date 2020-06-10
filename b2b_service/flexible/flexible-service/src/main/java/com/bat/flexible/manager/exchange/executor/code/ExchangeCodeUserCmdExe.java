package com.bat.flexible.manager.exchange.executor.code;

import com.bat.flexible.dao.exchange.ExchangeCodeUserDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeUserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCodeUserCmdExe {

    @Autowired
    private ExchangeCodeUserDOMapper exchangeCodeUserDOMapper;


    public void create(ExchangeCodeUserDO exchangeCodeUserDO) {
        exchangeCodeUserDOMapper.insert(exchangeCodeUserDO);
    }

    public void deleteById(Integer id) {
        exchangeCodeUserDOMapper.deleteByPrimaryKey(id);
    }


    /**
     * 前台用户进行兑换码解绑
     * @param exchangeCodeUserDO
     * @return
     */
    public void unbound(ExchangeCodeUserDO exchangeCodeUserDO) {
        exchangeCodeUserDOMapper.unboundExchange(exchangeCodeUserDO);
    }


}
