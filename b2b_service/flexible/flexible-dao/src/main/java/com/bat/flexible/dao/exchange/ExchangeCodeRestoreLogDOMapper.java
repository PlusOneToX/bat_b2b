package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeRestoreLogDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExchangeCodeRestoreLogDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCodeRestoreLogDO record);

    int insertSelective(ExchangeCodeRestoreLogDO record);

    ExchangeCodeRestoreLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCodeRestoreLogDO record);

    int updateByPrimaryKey(ExchangeCodeRestoreLogDO record);
}