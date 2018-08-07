package com.bat.financial.dao.basesetting;

import java.util.List;

import com.bat.financial.dao.basesetting.dataobject.CurrencyDO;

public interface CurrencyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CurrencyDO record);

    int insertSelective(CurrencyDO record);

    CurrencyDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CurrencyDO record);

    int updateByPrimaryKey(CurrencyDO record);

    List<CurrencyDO> listAll();

    CurrencyDO getByErpNo(String erpNo);

    CurrencyDO getByCurrencyCode(String currencyCode);

    CurrencyDO getCurrencyByErpNo(String erpNo);
}