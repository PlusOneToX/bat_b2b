package com.bat.distributor.dao.trade;

import com.bat.distributor.dao.trade.dataobject.TradeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TradeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TradeDO record);

    TradeDO selectByPrimaryKey(Integer id);

    List<TradeDO> selectAll();

    int updateByPrimaryKey(TradeDO record);

    Integer getTradeDistributorsCount(@Param("tradeId") Integer tradeId);

    void openTrade(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    List<TradeDO> listTrade(Map map);

    TradeDO getByDistributorId(@Param("distributorId") Integer distributorId);
}