package com.bat.order.dao.deliver;

import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDeliverBillDetailDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDeliverBillDetailDO record);

    int insertSelective(OrderDeliverBillDetailDO record);

    OrderDeliverBillDetailDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDeliverBillDetailDO record);

    int updateByPrimaryKey(OrderDeliverBillDetailDO record);

    List<OrderDeliverBillDetailDO> listByOrderDeliverBillId(Integer orderDeliverBillId);

    List<OrderDeliverBillDetailDO> listByOrderGoodsId(@Param("orderGoodsId") Integer orderGoodsId);

    void deleteByOrderDeliverBillIds(@Param("orderDeliverBillIdList") List<Integer> orderDeliverBillIdList);

    List<OrderDeliverBillDetailDO> listByOrderDeliverBillIdList(@Param("orderDeliverBillIdList") List<Integer> orderDeliverBillIdList);

    List<OrderDeliverBillDetailDO> listMorethenCreateTime();

    List<OrderDeliverBillDetailDO> listByOrderIdList(@Param("orderIdList") List<Integer> orderIdList);
}