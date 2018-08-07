package com.bat.order.dao.cost;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;

@Mapper
public interface OrderGoodsCustomerCostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderGoodsCustomerCostDO record);

    void insertList(List<OrderGoodsCustomerCostDO> records);

    int insertSelective(OrderGoodsCustomerCostDO record);

    OrderGoodsCustomerCostDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderGoodsCustomerCostDO record);

    int updateByPrimaryKey(OrderGoodsCustomerCostDO record);

    List<OrderGoodsCustomerCostDO> listByOrderIdAndCustomerId(@Param("orderId") Integer orderId,
        @Param("customerId") Integer customerId);

    List<OrderGoodsCustomerCostDO> listByOrderIdsAndCustomerId(@Param("orderIds") List<Integer> orderIds,
                                                              @Param("customerId") Integer customerId);

    OrderGoodsCustomerCostDO getByOrderGoodsId(@Param("orderGoodsId") Integer id);

    void batchUpdate(@Param("updateList")List<OrderGoodsCustomerCostDO> updateList);
}