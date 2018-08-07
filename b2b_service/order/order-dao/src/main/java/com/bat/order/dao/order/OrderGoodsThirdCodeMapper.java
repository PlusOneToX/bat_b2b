package com.bat.order.dao.order;

import java.util.List;

import com.bat.order.dao.order.dataobject.OrderGoodsThirdCodeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderGoodsThirdCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderGoodsThirdCodeDO record);

    void insertList(List<OrderGoodsThirdCodeDO> records);

    int insertSelective(OrderGoodsThirdCodeDO record);

    OrderGoodsThirdCodeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderGoodsThirdCodeDO record);

    int updateByPrimaryKey(OrderGoodsThirdCodeDO record);

    OrderGoodsThirdCodeDO findByCode(@Param("code") String code);

    List<OrderGoodsThirdCodeDO> listDeliverByWriteOff(@Param("writeOffFlag") Short writeOffFlag);

    void listUpdateWriteOff(List<OrderGoodsThirdCodeDO> orderGoodsThirdCodeDOS);
}