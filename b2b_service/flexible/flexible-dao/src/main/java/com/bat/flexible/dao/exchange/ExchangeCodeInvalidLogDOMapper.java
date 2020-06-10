package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeInvalidLogDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExchangeCodeInvalidLogDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCodeInvalidLogDO record);

    int insertSelective(ExchangeCodeInvalidLogDO record);

    ExchangeCodeInvalidLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCodeInvalidLogDO record);

    int updateByPrimaryKey(ExchangeCodeInvalidLogDO record);
}