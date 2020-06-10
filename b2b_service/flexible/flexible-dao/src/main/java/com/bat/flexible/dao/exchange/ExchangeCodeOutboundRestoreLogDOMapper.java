package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeOutboundRestoreLogDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExchangeCodeOutboundRestoreLogDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCodeOutboundRestoreLogDO record);

    int insertSelective(ExchangeCodeOutboundRestoreLogDO record);

    ExchangeCodeOutboundRestoreLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCodeOutboundRestoreLogDO record);

    int updateByPrimaryKey(ExchangeCodeOutboundRestoreLogDO record);
}