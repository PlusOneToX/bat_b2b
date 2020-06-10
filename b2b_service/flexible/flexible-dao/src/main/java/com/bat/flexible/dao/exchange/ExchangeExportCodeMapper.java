package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeExportCodeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExchangeExportCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeExportCodeDO record);

    int insertSelective(ExchangeExportCodeDO record);

    ExchangeExportCodeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeExportCodeDO record);

    int updateByPrimaryKey(ExchangeExportCodeDO record);

    void insertList(List<ExchangeExportCodeDO> exchangeExportCodeDOS);
}