package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeExportDO;
import com.bat.flexible.dao.exchange.co.ExchangeExportPageCO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExchangeExportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeExportDO record);

    int insertSelective(ExchangeExportDO record);

    ExchangeExportDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeExportDO record);

    int updateByPrimaryKey(ExchangeExportDO record);

    List<ExchangeExportPageCO> listCOByCondition(Integer exchangeId);
}