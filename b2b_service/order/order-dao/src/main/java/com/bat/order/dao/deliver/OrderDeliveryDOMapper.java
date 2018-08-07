package com.bat.order.dao.deliver;

import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDeliveryDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDeliveryDO record);

    int insertSelective(OrderDeliveryDO record);

    OrderDeliveryDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDeliveryDO record);

    int updateByPrimaryKey(OrderDeliveryDO record);

    OrderDeliveryDO getByOrderId(@Param("orderId") Integer orderId);

}