package com.bat.thirdparty.alibaba.taobao.dao;

import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeOrderRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaobaoTradeOrderRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaobaoTradeOrderRelevanceDO record);

    int insertSelective(TaobaoTradeOrderRelevanceDO record);

    TaobaoTradeOrderRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaobaoTradeOrderRelevanceDO record);

    int updateByPrimaryKey(TaobaoTradeOrderRelevanceDO record);

    List<TaobaoTradeOrderRelevanceDO> findByTid(@Param("tid")Long tid);
}