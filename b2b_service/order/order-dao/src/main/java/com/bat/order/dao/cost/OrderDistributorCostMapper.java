package com.bat.order.dao.cost;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;

@Mapper
public interface OrderDistributorCostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDistributorCostDO record);

    void insertList(List<OrderGoodsDistributorCostDO> records);

    int insertSelective(OrderDistributorCostDO record);

    OrderDistributorCostDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDistributorCostDO record);

    int updateByPrimaryKey(OrderDistributorCostDO record);

    void updateOrderRefundedAmount(@Param("orderId") Integer orderId, @Param("distributorId") Integer distributorId,
        @Param("refundedAmount") BigDecimal refundedAmount);

    void updateList(List<OrderDistributorCostDO> records);

    List<OrderDistributorCostDO> listByOrderId(@Param("orderId") Integer orderId);

    OrderDistributorCostDO getErpOrderByOrderId(@Param("orderId") Integer orderId);

    OrderDistributorCostDO getByOrderIdAndDistributorId(@Param("orderId") Integer orderId,
        @Param("distributorId") Integer distributorId);

    List<OrderDistributorCostDO> listByOrderIdsAndDistributorId(@Param("orderIds") List<Integer> orderIds,
        @Param("distributorId") Integer distributorId);

    List<OrderDistributorCostDO> listByOrderErpNo(@Param("orderErpNo") String orderErpNo);

    List<OrderDistributorCostDO> listByOrderIdsAndErpFlag(@Param("orderIds") List<Integer> orderIds);

    List<OrderDistributorCostDO> getDirectPayInfoByOrderId(@Param("orderId") Integer orderId);

    List<OrderDistributorCostDO> listByOrderIdsAndTreeNode(@Param("orderIds") List<Integer> orderIds,
        @Param("treeNode") Integer treeNode);

    void modifyPaymentVoucher(@Param("primaryDistributorId") Integer primaryDistributorId,
        @Param("orderId") Integer orderId, @Param("outTradeNo") String outTradeNo);
}