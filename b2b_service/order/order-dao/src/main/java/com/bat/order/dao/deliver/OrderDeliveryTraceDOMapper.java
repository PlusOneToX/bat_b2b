package com.bat.order.dao.deliver;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.order.dao.deliver.dataobject.OrderDeliveryTraceDO;

@Mapper
public interface OrderDeliveryTraceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDeliveryTraceDO record);

    int insertSelective(OrderDeliveryTraceDO record);

    OrderDeliveryTraceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDeliveryTraceDO record);

    int updateByPrimaryKey(OrderDeliveryTraceDO record);

    List<OrderDeliveryTraceDO> listByOrderDeliverBillId(@Param("orderDeliverBillId") Integer id);

    void insertBatch(@Param("dos") List<OrderDeliveryTraceDO> dos);

    void deleteByOrderDeliverBillId(Integer orderDeliverBillId);
}