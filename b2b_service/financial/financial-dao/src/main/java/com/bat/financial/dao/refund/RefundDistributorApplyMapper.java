package com.bat.financial.dao.refund;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bat.financial.dao.refund.dataobject.RefundDistributorApplyDO;

public interface RefundDistributorApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundDistributorApplyDO record);

    int insertSelective(RefundDistributorApplyDO record);

    RefundDistributorApplyDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RefundDistributorApplyDO record);

    int updateByPrimaryKey(RefundDistributorApplyDO record);

    List<RefundDistributorApplyDO> selectByParams(@Param("params") Map<String, Object> map);

    RefundDistributorApplyDO getLikeRemark(@Param("outRefundNo") String outRefundNo);
}