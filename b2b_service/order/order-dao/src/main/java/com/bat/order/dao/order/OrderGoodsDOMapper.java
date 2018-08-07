package com.bat.order.dao.order;

import java.util.List;
import java.util.Map;

import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO2;
import com.bat.order.dao.order.dataobject.OrderGoodsDetailDTO;
import com.bat.order.dao.order.dataobject.OrderGoodsVmiDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderGoodsDOMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByIds(@Param("ids") List<Integer> ids);

    int insert(OrderGoodsDO record);

    void insertList(List<OrderGoodsDO> records);

    int insertSelective(OrderGoodsDO record);

    OrderGoodsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderGoodsDO record);

    int updateByPrimaryKey(OrderGoodsDO record);

    void updateList(List<OrderGoodsDO> records);

    List<OrderGoodsDO> listByOrderId(@Param("orderId") Integer orderId);

    List<OrderGoodsDO> listByIds(List<Integer> ids);

    List<OrderGoodsDO> listByIdsAndOrderStatus(List<Integer> ids);

    OrderGoodsDO findByOrderIdAndItemIdAndSerialNumber(@Param("orderId") Integer orderId,
        @Param("itemId") Integer itemId, @Param("serialNumber") Integer serialNumber);

    List<OrderGoodsVmiDO> listVmiByParam(@Param("params") Map<String, Object> map);

    List<OrderGoodsDetailDTO> listOrderGoodsDetailByOrderId(@Param("orderId") Integer orderId);

    List<OrderGoodsDO> listByOrderIdList(@Param("orderIdList") List<Integer> orderIdList);

    List<OrderGoodsDO2> listOrderGoodsDO2ByOrderErpNos(@Param("orderErpNos") List<String> orderErpNos);
}