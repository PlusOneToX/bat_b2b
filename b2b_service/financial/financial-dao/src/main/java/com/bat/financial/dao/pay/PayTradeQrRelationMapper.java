package com.bat.financial.dao.pay;

import com.bat.financial.dao.pay.dataobject.PayTradeQrRelationDO;

public interface PayTradeQrRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayTradeQrRelationDO record);

    int insertSelective(PayTradeQrRelationDO record);

    PayTradeQrRelationDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayTradeQrRelationDO record);

    int updateByPrimaryKey(PayTradeQrRelationDO record);

    PayTradeQrRelationDO getByOutTradeNo(String outTradeNo);
}