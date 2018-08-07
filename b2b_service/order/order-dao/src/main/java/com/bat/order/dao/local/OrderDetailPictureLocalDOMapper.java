package com.bat.order.dao.local;

import com.bat.order.dao.local.dataobject.OrderDetailPictureLocalDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailPictureLocalDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDetailPictureLocalDO record);

    int insertSelective(OrderDetailPictureLocalDO record);

    OrderDetailPictureLocalDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDetailPictureLocalDO record);

    int updateByPrimaryKey(OrderDetailPictureLocalDO record);

    OrderDetailPictureLocalDO getByOrderGoodsDiyId(@Param("orderGoodsDiyId") Integer orderGoodsDiyId);

    List<OrderDetailPictureLocalDO> listByOrderId(@Param("orderId") Integer orderId);

    void createList(@Param("createList") List<OrderDetailPictureLocalDO> createList);

    void batchUpdate(@Param("updateList")List<OrderDetailPictureLocalDO> updateList);
}