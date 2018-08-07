package com.bat.order.dao.stock;

import java.util.List;

import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDTO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDTO2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO2;

@Mapper
public interface OrderGoodsStockDOMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByOrderId(@Param("orderId") Integer orderId);

    void deleteByOrderIds(@Param("orderIds") List<Integer> orderIds);

    void deleteByOrderGoodsIds(@Param("orderGoodsIds") List<Integer> orderGoodsIds);

    void deleteByIds(@Param("ids") List<Integer> ids);

    int insert(OrderGoodsStockDO record);

    int insertList(List<OrderGoodsStockDO> records);

    int insertSelective(OrderGoodsStockDO record);

    OrderGoodsStockDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderGoodsStockDO record);

    int updateByPrimaryKey(OrderGoodsStockDO record);

    List<OrderGoodsStockDTO> listDTOByOrderIdAndSerialNumberList(@Param("orderId") Integer orderId,
                                                                 @Param("serialNumberList") List<Integer> serialNumberList);

    List<OrderGoodsStockDO> listByOrderId(@Param("orderId") Integer orderId);

    List<OrderGoodsStockDO> listByOrderIds(@Param("orderIds") List<Integer> orderIds);

    List<OrderGoodsStockDTO2> listDTOByCondition(@Param("warehouseId") Integer warehouseId,
                                                 @Param("lockType") Short lockType, @Param("itemId") Integer itemId);

    void batchUpdate(@Param("updateList") List<OrderGoodsStockDO> updateList);

    List<OrderGoodsStockDO> sumByWarehouseIdAndLockTypeGroupByItemId(@Param("lockType") Short lockType,
        @Param("warehouseId") Integer warehousesId, @Param("itemId") Integer itemId);

    List<OrderGoodsStockDO> sumByItemIds(@Param("itemIds") List<Integer> itemIds);

    List<OrderGoodsStockDO2> listByOrderErpNos(@Param("orderErpNos") List<String> orderErpNos);

    void batchUpdateOrderGoodsStock(
        @Param("changeOrderGoodsStockDOS") List<OrderGoodsStockDO2> changeOrderGoodsStockDOS);
}