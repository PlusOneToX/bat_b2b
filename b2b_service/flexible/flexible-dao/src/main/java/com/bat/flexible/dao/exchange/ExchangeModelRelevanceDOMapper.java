package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeModelRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeModelRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeModelRelevanceDO record);

    int insertSelective(ExchangeModelRelevanceDO record);

    ExchangeModelRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeModelRelevanceDO record);

    int updateByPrimaryKey(ExchangeModelRelevanceDO record);

    List<ExchangeModelRelevanceDO> listByExchangeId(@Param("exchangeId") Integer exchangeId);

    ExchangeModelRelevanceDO findOneByExchangeIdAndModelId(@Param("exchangeId") Integer exchangeId,@Param("modelId") Integer modelId);
}