package com.bat.flexible.dao.exchange;


import com.bat.flexible.dao.exchange.dataobject.ExchangeCardTransferDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExchangeCardTransferMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCardTransferDO record);

    int insertSelective(ExchangeCardTransferDO record);

    ExchangeCardTransferDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCardTransferDO record);

    int updateByPrimaryKey(ExchangeCardTransferDO record);

    ExchangeCardTransferDO selectByExchangeId(@Param("exchangeId")Integer exchangeId);
}