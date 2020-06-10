package com.bat.flexible.dao.exchange;


import com.bat.flexible.dao.exchange.dataobject.ExchangeSpecialReleaseDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeSpecialReleaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeSpecialReleaseDO record);

    int insertSelective(ExchangeSpecialReleaseDO record);

    ExchangeSpecialReleaseDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeSpecialReleaseDO record);

    int updateByPrimaryKey(ExchangeSpecialReleaseDO record);

    int countReceive(@Param("exchangeSpecialId")Integer exchangeSpecialId, @Param("distributorId")Integer distributorId, @Param("receiveUserId")Integer receiveUserId);

    void insertList(List<ExchangeSpecialReleaseDO> list);

    ExchangeSpecialReleaseDO findCodeRecordBySpecialDistributorId(@Param("exchangeSpecialDistributorId")Integer exchangeSpecialDistributorId);
}