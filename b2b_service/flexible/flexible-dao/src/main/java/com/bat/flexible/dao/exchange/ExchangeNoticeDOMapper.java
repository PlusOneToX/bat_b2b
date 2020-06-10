package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeNoticeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ExchangeNoticeDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeNoticeDO record);

    int insertSelective(ExchangeNoticeDO record);

    ExchangeNoticeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeNoticeDO record);

    int updateByPrimaryKeyWithBLOBs(ExchangeNoticeDO record);

    int updateByPrimaryKey(ExchangeNoticeDO record);

    List<ExchangeNoticeDO> listAllDesc();
}