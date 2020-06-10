package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeEntityRuleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExchangeEntityRuleDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeEntityRuleDO record);

    int insertSelective(ExchangeEntityRuleDO record);

    ExchangeEntityRuleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeEntityRuleDO record);

    int updateByPrimaryKey(ExchangeEntityRuleDO record);

    ExchangeEntityRuleDO getByExchangeId(@Param("exchangeId") Integer exchangeId);
}