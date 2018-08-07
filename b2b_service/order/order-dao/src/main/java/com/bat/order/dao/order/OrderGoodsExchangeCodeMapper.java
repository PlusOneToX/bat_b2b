package com.bat.order.dao.order;

import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeDO;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeListDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderGoodsExchangeCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderGoodsExchangeCodeDO record);

    int insertList(List<OrderGoodsExchangeCodeDO> records);

    int insertSelective(OrderGoodsExchangeCodeDO record);

    OrderGoodsExchangeCodeDO selectByPrimaryKey(Integer id);

    List<OrderGoodsExchangeCodeDO> listByOrderId(Integer orderId);

    int updateByPrimaryKeySelective(OrderGoodsExchangeCodeDO record);

    int updateByPrimaryKey(OrderGoodsExchangeCodeDO record);

    void updateList(List<OrderGoodsExchangeCodeDO> records);

    OrderGoodsExchangeCodeDO getByOrderIdAndOrderGoodsId(@Param("orderId") Integer id,
        @Param("orderGoodsId") Integer id1);

    List<OrderGoodsExchangeCodeListDO> listDOByCondition(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
                                                         @Param("orderStatus") Short orderStatus, @Param("content") String content);
}