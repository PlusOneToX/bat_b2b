package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.co.ExchangeCardPageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeCardDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCardDO record);

    int insertSelective(ExchangeCardDO record);

    ExchangeCardDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCardDO record);

    int updateByPrimaryKey(ExchangeCardDO record);

    List<ExchangeCardDO> listByItemIdAndStatusList(@Param("itemId") Integer itemId,@Param("statusList")List<Short> statusList);

    void createStartExchangeEvent(@Param("time") String time, @Param("exchangeId") Integer exchangeId);

    void createEndExchangeEvent(@Param("time") String time, @Param("exchangeId") Integer exchangeId);

    List<ExchangeCardDO> betweenStatusOrderByStatusDesc();

    List<ExchangeCardPageCO> listCOByCondition(@Param("type") Short type, @Param("status") Short status, @Param("exchangeWay") Short exchangeWay,
                                               @Param("codeName") String codeName, @Param("isEntity")Short isEntity);

    List<ExchangeCardDO> listByIdList(@Param("exchangeIdList") List<Integer> exchangeIdList);

    ExchangeCardDO findQuanyiByDistributorIdAndMaterialId(@Param("distributorId")Integer distributorId,@Param("materialId") Integer materialId);

    Integer countQuanyiByDistributorIdAndMaterialId(@Param("distributorId")Integer distributorId,@Param("materialId") Integer materialId);

}