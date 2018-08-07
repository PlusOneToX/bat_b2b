package com.bat.order.dao.deliver;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillListDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDeliverBillDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDeliverBillDO record);

    int insertSelective(OrderDeliverBillDO record);

    OrderDeliverBillDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDeliverBillDO record);

    int updateByPrimaryKey(OrderDeliverBillDO record);

    List<OrderDeliverBillDO> listByOrderId(@Param("orderId") Integer orderId);

    List<OrderDeliverBillListDO> listByOrderDeliverByParam(@Param("params") Map<String, Object> map);

    List<OrderDeliverBillDO> listByDeliverErpNo(@Param("deliverErpNo") String deliverErpNo);

    void batchDelete(@Param("idList") List<Integer> idList);

    void batchUpdate(@Param("orderDeliverBillDOList") List<OrderDeliverBillDO> orderDeliverBillDOList);

    List<OrderDeliverBillDO> findNOLogisticsNoDeliverGoods(@Param("date") Date date);

    List<OrderDeliverBillDO> listByOrderIdList(@Param("orderIdList") List<Integer> orderIdList);

    List<Integer> getNotErpOrderDeliverBillIds( @Param("startTime") Date startTime);
}