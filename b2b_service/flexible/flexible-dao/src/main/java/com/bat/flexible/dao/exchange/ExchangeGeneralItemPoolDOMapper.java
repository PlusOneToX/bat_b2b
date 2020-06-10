package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeGeneralItemPoolDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeGeneralItemPoolDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeGeneralItemPoolDO record);

    int insertSelective(ExchangeGeneralItemPoolDO record);

    ExchangeGeneralItemPoolDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeGeneralItemPoolDO record);

    int updateByPrimaryKey(ExchangeGeneralItemPoolDO record);

    List<ExchangeGeneralItemPoolDO> listByItemCodeDimAndOpenFlag(@Param("content") String content,@Param("openFlag")Short openFlag);
}