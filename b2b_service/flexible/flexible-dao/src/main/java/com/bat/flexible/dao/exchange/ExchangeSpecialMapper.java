package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeSpecialDO;
import com.bat.flexible.dao.exchange.co.ExchangeSpecialPageCO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeSpecialMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeSpecialDO record);

    int insertSelective(ExchangeSpecialDO record);

    ExchangeSpecialDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeSpecialDO record);

    int updateByPrimaryKey(ExchangeSpecialDO record);

    List<ExchangeSpecialPageCO> listCOByCondition(@Param("title")String title, @Param("distributorId")Integer distributorId,@Param("activityPlatform")Short activityPlatform,@Param("type")Short type);
}