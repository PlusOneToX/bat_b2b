package com.bat.financial.dao.refund;

import org.apache.ibatis.annotations.Param;

import com.bat.financial.dao.refund.dataobject.RefundBillsCustomerDO;

public interface RefundBillsCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundBillsCustomerDO record);

    int insertSelective(RefundBillsCustomerDO record);

    RefundBillsCustomerDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RefundBillsCustomerDO record);

    int updateByPrimaryKey(RefundBillsCustomerDO record);

    RefundBillsCustomerDO getByOutRefundNo(@Param("outRefundNo") String outRefundNo);
}