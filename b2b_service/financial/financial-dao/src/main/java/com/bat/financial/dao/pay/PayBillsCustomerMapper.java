package com.bat.financial.dao.pay;

import java.math.BigDecimal;
import java.util.List;

import com.bat.financial.dao.pay.dataobject.PayBillsCustomerDO;
import org.apache.ibatis.annotations.Param;

public interface PayBillsCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayBillsCustomerDO record);

    int insertSelective(PayBillsCustomerDO record);

    PayBillsCustomerDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayBillsCustomerDO record);

    int updateByPrimaryKey(PayBillsCustomerDO record);

    PayBillsCustomerDO getByOutTradeNo(String outTradeNo);

    PayBillsCustomerDO getByOrderId(String orderId);

    PayBillsCustomerDO getByOrderIdAndCustomerId(@Param("orderId") String orderId,
        @Param("customerId") Integer customerId);

    void deleteByOutTradeNo(String outTradeNo);

    void deleteByOrderId(@Param("orderId") String orderId, @Param("payerId") Integer payerId);

    PayBillsCustomerDO getByOutTradeNoAndTotalFee(@Param("outTradeNo") String outTradeNo,
        @Param("totalFee") BigDecimal totalFee);

    List<PayBillsCustomerDO> listByOnlineTradeNo(@Param("onlineTradeNo") String onlineTradeNo);
}