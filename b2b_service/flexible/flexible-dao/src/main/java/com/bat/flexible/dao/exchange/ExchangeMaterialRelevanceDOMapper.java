package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeMaterialRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeMaterialRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeMaterialRelevanceDO record);

    int insertSelective(ExchangeMaterialRelevanceDO record);

    ExchangeMaterialRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeMaterialRelevanceDO record);

    int updateByPrimaryKey(ExchangeMaterialRelevanceDO record);

    List<ExchangeMaterialRelevanceDO> listByExchangeId(@Param("exchangeId") Integer exchangeId);

    ExchangeMaterialRelevanceDO findByExchangeIdAndMaterialId(@Param("exchangeId")Integer exchangeId, @Param("materialId") Integer materialId);
}