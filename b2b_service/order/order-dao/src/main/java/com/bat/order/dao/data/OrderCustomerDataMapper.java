package com.bat.order.dao.data;

import java.util.List;

import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderCustomerDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderCustomerDataDO record);

    int insertSelective(OrderCustomerDataDO record);

    OrderCustomerDataDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderCustomerDataDO record);

    int updateByPrimaryKey(OrderCustomerDataDO record);

    void updateOrderPayStatus(@Param("orderId") Integer orderId, @Param("customerId") Integer customerId,
        @Param("payStatus") Short payStatus);

    void updateList(List<OrderCustomerDataDO> records);

    OrderCustomerDataDO getByOrderIdAndCustomerId(@Param("orderId") Integer orderId,
        @Param("customerId") Integer customerId);

    OrderCustomerDataDO getByOrderId(@Param("orderId") Integer orderId);

    List<OrderCustomerDataDO> listByOrderIdsAndCustomerId(@Param("orderIds") List<Integer> orderIds,
        @Param("customerId") Integer customerId);

    void cancelCustomerOrderById(Integer id);
}