package com.bat.financial.dao.pay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.bat.financial.dao.pay.dataobject.PayBillsDistributorDO;
import org.apache.ibatis.annotations.Param;

public interface PayBillsDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayBillsDistributorDO record);

    int insertSelective(PayBillsDistributorDO record);

    PayBillsDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayBillsDistributorDO record);

    int updateByPrimaryKey(PayBillsDistributorDO record);

    PayBillsDistributorDO getByOutTradeNo(String outTradeNo);

    PayBillsDistributorDO getByOrderId(String orderId);

    PayBillsDistributorDO getByOrderIdAndDistributorId(@Param("orderId") String orderId,
        @Param("distributorId") Integer distributorId);

    void deleteByOutTradeNo(String outTradeNo);

    void deleteByOrderId(@Param("orderId") String orderId, @Param("payerId") Integer payerId);

    List<PayBillsDistributorDO> selectByParams(@Param("params") Map<String, Object> map);

    PayBillsDistributorDO getByOutTradeNoAndTotalFee(@Param("outTradeNo") String outTradeNo,
        @Param("totalFee") BigDecimal totalFee);
}