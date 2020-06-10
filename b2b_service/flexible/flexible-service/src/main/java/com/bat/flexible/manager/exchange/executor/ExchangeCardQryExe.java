package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.exchange.ExchangeCardDOMapper;
import com.bat.flexible.dao.exchange.co.ExchangeCardPageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeCardQryExe {

    @Autowired
    private ExchangeCardDOMapper exchangeCardDOMapper;

    public ExchangeCardDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_EXCHANGE_CARD)+MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL));
        }
        ExchangeCardDO exchangeCardDO = exchangeCardDOMapper.selectByPrimaryKey(id);
        if(exchangeCardDO ==null){
            throw new FlexibleCustomException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_EXCHANGE_CARD)+MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR));
        }
        return exchangeCardDO;
    }

    public List<ExchangeCardDO> listByItemIdAndStatusList(Integer itemId,List<Short> statusList) {
        return exchangeCardDOMapper.listByItemIdAndStatusList(itemId,statusList);
    }

    public ExchangeCardDO getDefaultExchange(Integer exchangeId) {
        List<ExchangeCardDO> cardDOList = exchangeCardDOMapper.betweenStatusOrderByStatusDesc();
        if(cardDOList ==null || cardDOList.size()==0){
            return null;
        }
        if(exchangeId !=null){
            for(int x=0;x<cardDOList.size();x++){
                if(cardDOList.get(x).getId() - exchangeId ==0){
                    return cardDOList.get(x);
                }
            }
        }
        return cardDOList.get(0);
    }

    public List<ExchangeCardPageCO> listCOByCondition(Short type, Short status, Short exchangeWay, String codeName,Short isEntity) {
        return exchangeCardDOMapper.listCOByCondition(type,status,exchangeWay,codeName,isEntity);
    }

    public List<ExchangeCardDO> listByIdList(List<Integer> exchangeIdList) {
        return exchangeCardDOMapper.listByIdList(exchangeIdList);
    }

    public ExchangeCardDO findQuanyiByDistributorIdAndMaterialId(Integer distributorId, Integer materialId) {
        return exchangeCardDOMapper.findQuanyiByDistributorIdAndMaterialId(distributorId,materialId);
    }

    public Integer countQuanyiByDistributorIdAndMaterialId(Integer distributorId, Integer materialId) {
        return exchangeCardDOMapper.countQuanyiByDistributorIdAndMaterialId(distributorId,materialId);
    }
}
