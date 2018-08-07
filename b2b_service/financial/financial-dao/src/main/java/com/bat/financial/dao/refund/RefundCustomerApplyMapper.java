package com.bat.financial.dao.refund;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bat.financial.dao.refund.dataobject.RefundCustomerApplyDO;

public interface RefundCustomerApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundCustomerApplyDO record);

    int insertSelective(RefundCustomerApplyDO record);

    RefundCustomerApplyDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RefundCustomerApplyDO record);

    int updateByPrimaryKey(RefundCustomerApplyDO record);

    List<RefundCustomerApplyDO> selectByParams(@Param("params") Map<String, Object> map);

    RefundCustomerApplyDO getLikeRemark(@Param("outRefundNo") String outRefundNo);
}