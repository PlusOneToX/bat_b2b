package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeTransferRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExchangeCodeTransferRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCodeTransferRecordDO record);

    int insertSelective(ExchangeCodeTransferRecordDO record);

    ExchangeCodeTransferRecordDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCodeTransferRecordDO record);

    int updateByPrimaryKey(ExchangeCodeTransferRecordDO record);

    ExchangeCodeTransferRecordDO selectByCondition(@Param("exchangeCodeId")Integer exchangeCodeId, @Param("fromUserId")Integer fromUserId, @Param("receiveFlag")Short receiveFlag);
}