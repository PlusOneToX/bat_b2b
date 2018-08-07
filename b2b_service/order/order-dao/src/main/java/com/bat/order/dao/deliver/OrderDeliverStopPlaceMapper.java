package com.bat.order.dao.deliver;

import com.bat.order.dao.deliver.dataobject.OrderDeliverStopPlaceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDeliverStopPlaceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDeliverStopPlaceDO record);

    int insertSelective(OrderDeliverStopPlaceDO record);

    OrderDeliverStopPlaceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDeliverStopPlaceDO record);

    int updateByPrimaryKey(OrderDeliverStopPlaceDO record);

    int findMatch(@Param("placeName")String placeName);

    List<OrderDeliverStopPlaceDO> list(@Param("content")String content);

    List<OrderDeliverStopPlaceDO> selectAll();
}