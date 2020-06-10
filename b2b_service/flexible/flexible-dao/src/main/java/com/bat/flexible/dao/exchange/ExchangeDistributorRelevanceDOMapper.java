package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeDistributorRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeDistributorRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeDistributorRelevanceDO record);

    int insertSelective(ExchangeDistributorRelevanceDO record);

    ExchangeDistributorRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeDistributorRelevanceDO record);

    int updateByPrimaryKey(ExchangeDistributorRelevanceDO record);

    List<ExchangeDistributorRelevanceDO> listByExchangeIdAndDistributorId(@Param("exchangeId") Integer exchangeId,@Param("distributorId")Integer distributorId);
}