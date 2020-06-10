package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.dao.exchange.ExchangeCodeDOMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeCodeCmdExe {

    @Autowired
    private ExchangeCodeDOMapper exchangeCodeDOMapper;

    public void create(ExchangeCodeDO exchangeCodeDO) {
        exchangeCodeDOMapper.insert(exchangeCodeDO);
    }

    public void deleteById(Integer id) {
        exchangeCodeDOMapper.deleteByPrimaryKey(id);
    }


    public void createEndExchangeEvent(String time, Integer exchangeId) {
        exchangeCodeDOMapper.createEndExchangeEvent(time,exchangeId);
    }

    public void update(ExchangeCodeDO code) {
        exchangeCodeDOMapper.updateByPrimaryKey(code);
    }

    /**
     * 根据兑换卡活动id给未设置工厂批次的设置工厂批次id
     * @param exchangeId
     * @param factoryId
     */
    public void updateExchangeFactoryId(Integer exchangeId, Integer factoryId) {
        exchangeCodeDOMapper.updateExchangeFactoryId(exchangeId,factoryId);
    }

    public void batchCreate(List<ExchangeCodeDO> codeDOList) {
        exchangeCodeDOMapper.batchCreate(codeDOList);
    }

    public void batchCreateContainKey(List<ExchangeCodeDO> codeDOList) {
        exchangeCodeDOMapper.batchCreateContainKey(codeDOList);
    }

    public void updateId(Integer oldId, Integer newId) {
        exchangeCodeDOMapper.updateId(oldId,newId);
    }

    public void batchUpdate(List<ExchangeCodeDO> updateList) {
        exchangeCodeDOMapper.batchUpdate(updateList);
    }

    /**
     * 后台兑换码解绑
     * @param exchangeCodeDO
     * @return
     */
    public void unbound(ExchangeCodeDO exchangeCodeDO) {
        exchangeCodeDOMapper.unboundExchange(exchangeCodeDO);
    }
    //后台兑换码解绑子属性 判断兑换卡是否被领取
    public ExchangeCodeDO selectExchangeUserRecordById(ExchangeCodeDO exchangeCodeDO) {
        return exchangeCodeDOMapper.selectExchangeUserRecordById(exchangeCodeDO);
    }
}
