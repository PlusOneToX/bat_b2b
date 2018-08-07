package com.bat.order.dao.cost;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;

@Mapper
public interface OrderGoodsDistributorCostMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByOrderGoodsIds(@Param("orderGoodsIds") List<Integer> orderGoodsIds);

    int insert(OrderGoodsDistributorCostDO record);

    void insertList(List<OrderGoodsDistributorCostDO> records);

    int insertSelective(OrderGoodsDistributorCostDO record);

    OrderGoodsDistributorCostDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderGoodsDistributorCostDO record);

    int updateByPrimaryKey(OrderGoodsDistributorCostDO record);

    void updateList(List<OrderGoodsDistributorCostDO> records);

    Integer checkIsGroup(@Param("orderId") Integer orderId);

    OrderGoodsDistributorCostDO getByOrderGoodsIdAndDistributorId(@Param("orderGoodsId") Integer orderGoodsIds,
        @Param("distributorId") Integer distributorId);

    List<OrderGoodsDistributorCostDO> listByOrderId(@Param("orderId") Integer orderId);

    List<OrderGoodsDistributorCostDO> listByOrderGoodsId(Integer id);

    List<OrderGoodsDistributorCostDO> listByOrderIdAndDistributorId(@Param("orderId") Integer orderId,
        @Param("distributorId") Integer distributorId);

    List<OrderGoodsDistributorCostDO> listByOrderGoodsIdsAndDistributorId(
        @Param("orderGoodsIds") List<Integer> orderGoodsIds, @Param("distributorId") Integer distributorId);

    List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostByOrderIdsAndDistributorId(
        @Param("orderGoodsIds") List<Integer> orderIds, @Param("distributorId") Integer distributorId);
}