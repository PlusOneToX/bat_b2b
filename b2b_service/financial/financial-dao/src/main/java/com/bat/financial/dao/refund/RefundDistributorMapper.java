package com.bat.financial.dao.refund;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bat.financial.dao.refund.dataobject.RefundDistributorDO;

public interface RefundDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RefundDistributorDO record);

    int insertSelective(RefundDistributorDO record);

    RefundDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RefundDistributorDO record);

    int updateByPrimaryKey(RefundDistributorDO record);

    List<RefundDistributorDO> selectByParams(@Param("params") Map<String, Object> map);

}