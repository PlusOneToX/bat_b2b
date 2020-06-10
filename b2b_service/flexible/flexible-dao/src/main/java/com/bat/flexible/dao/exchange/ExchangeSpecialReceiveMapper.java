package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeSpecialReceiveDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExchangeSpecialReceiveMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeSpecialReceiveDO record);

    int insertSelective(ExchangeSpecialReceiveDO record);

    ExchangeSpecialReceiveDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeSpecialReceiveDO record);

    int updateByPrimaryKey(ExchangeSpecialReceiveDO record);
}