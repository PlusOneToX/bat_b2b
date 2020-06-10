package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeSyncBackLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeCodeSyncBackLogDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCodeSyncBackLogDO record);

    int insertSelective(ExchangeCodeSyncBackLogDO record);

    ExchangeCodeSyncBackLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCodeSyncBackLogDO record);

    int updateByPrimaryKey(ExchangeCodeSyncBackLogDO record);

    ExchangeCodeSyncBackLogDO findOneByBoxCodeAndStatus(@Param("boxCode") String boxCode,@Param("status") Short status);

    List<ExchangeCodeSyncBackLogDO> listUnUseBoxCodeNumBetween(@Param("start") Integer start,@Param("end") Integer end,@Param("itemId") Integer itemId);

    ExchangeCodeSyncBackLogDO findLastByItemId(@Param("itemId") Integer itemId);

    void batchInsert(@Param("exchangeCodeSyncBackLogDOList") List<ExchangeCodeSyncBackLogDO> exchangeCodeSyncBackLogDOList);
}