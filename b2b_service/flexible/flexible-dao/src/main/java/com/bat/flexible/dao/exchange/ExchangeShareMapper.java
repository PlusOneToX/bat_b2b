package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.co.ExchangeSharePageCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeShareDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ExchangeShareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeShareDO record);

    int insertSelective(ExchangeShareDO record);

    ExchangeShareDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeShareDO record);

    int updateByPrimaryKey(ExchangeShareDO record);

    List<ExchangeSharePageCO> listCOByCondition(@Param("activityPlatform")Short activityPlatform, @Param("preferName")String preferName, @Param("seat")Short seat);

    ExchangeShareDO findSuitable(@Param("activityPlatform")Short activityPlatform, @Param("seat")Short seat);
}