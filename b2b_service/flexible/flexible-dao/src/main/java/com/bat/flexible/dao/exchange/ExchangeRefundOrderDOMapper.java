package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeRefundOrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ExchangeRefundOrderDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeRefundOrderDO record);

    int insertSelective(ExchangeRefundOrderDO record);

    ExchangeRefundOrderDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeRefundOrderDO record);

    int updateByPrimaryKey(ExchangeRefundOrderDO record);


    ExchangeRefundOrderDO getByRefundNo(@Param("refundNo") String refundNo);
}