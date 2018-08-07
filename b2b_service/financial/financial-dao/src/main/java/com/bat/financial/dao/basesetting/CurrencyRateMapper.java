package com.bat.financial.dao.basesetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bat.financial.dao.basesetting.dataobject.CurrencyRateDO;
import org.apache.ibatis.annotations.Param;

public interface CurrencyRateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CurrencyRateDO record);

    int insertSelective(CurrencyRateDO record);

    CurrencyRateDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CurrencyRateDO record);

    int updateByPrimaryKey(CurrencyRateDO record);

    List<CurrencyRateDO> listAll();

    CurrencyRateDO getByBegDate(Date begDate);

    CurrencyRateDO getCurrencyRateByForToCurrencyCode(@Param("forCurrencyCode") String forCurrencyCode,
        @Param("toCurrencyCode") String toCurrencyCode);

    List<CurrencyRateDO> selectByParams(@Param("params") Map<String, Object> map);
}