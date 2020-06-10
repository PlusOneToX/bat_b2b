package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeFactoryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExchangeFactoryDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeFactoryDO record);

    int insertSelective(ExchangeFactoryDO record);

    ExchangeFactoryDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeFactoryDO record);

    int updateByPrimaryKey(ExchangeFactoryDO record);
}