package com.bat.financial.dao.refund;

import com.bat.financial.dao.refund.dataobject.RefundBillsDistributorDO;
import org.apache.ibatis.annotations.Param;

public interface RefundBillsDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundBillsDistributorDO record);

    int insertSelective(RefundBillsDistributorDO record);

    RefundBillsDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RefundBillsDistributorDO record);

    int updateByPrimaryKey(RefundBillsDistributorDO record);

    RefundBillsDistributorDO getByOutRefundNo(@Param("outRefundNo") String outRefundNo);
}