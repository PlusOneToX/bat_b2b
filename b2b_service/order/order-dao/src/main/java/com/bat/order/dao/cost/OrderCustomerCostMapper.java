package com.bat.order.dao.cost;

import java.math.BigDecimal;
import java.util.List;

import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderCustomerCostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderCustomerCostDO record);

    int insertSelective(OrderCustomerCostDO record);

    OrderCustomerCostDO selectByPrimaryKey(Integer id);

    OrderCustomerCostDO getByOrderIdAndCustomerId(@Param("orderId") Integer orderId,
        @Param("customerId") Integer customerId);

    OrderCustomerCostDO getByOrderId(@Param("orderId") Integer orderId);

    List<OrderCustomerCostDO> listByOrderIdsAndCustomerId(@Param("orderIds") List<Integer> orderIds,
        @Param("customerId") Integer customerId);

    int updateByPrimaryKeySelective(OrderCustomerCostDO record);

    int updateByPrimaryKey(OrderCustomerCostDO record);

    void updateOrderRefundedAmount(@Param("orderId") Integer orderId, @Param("customerId") Integer customerId,
        @Param("refundedAmount") BigDecimal refundedAmount);

    void updateList(List<OrderCustomerCostDO> records);
}