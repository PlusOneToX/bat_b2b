package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangePictureRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangePictureRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangePictureRelevanceDO record);

    int insertSelective(ExchangePictureRelevanceDO record);

    ExchangePictureRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangePictureRelevanceDO record);

    int updateByPrimaryKey(ExchangePictureRelevanceDO record);

    List<ExchangePictureRelevanceDO> listByExchangeId(@Param("exchangeId") Integer exchangeId);

    ExchangePictureRelevanceDO findOneByExchangeId(@Param("exchangeId")Integer exchangeId);

    ExchangePictureRelevanceDO findByExchangeIdAndPictureId(@Param("exchangeId") Integer exchangeId,@Param("pictureId") Integer pictureId);
}