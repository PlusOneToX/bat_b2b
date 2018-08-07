package com.bat.order.api.stock.api;

import com.bat.order.api.stock.dto.OrderGoodsStockQry;
import com.bat.order.api.stock.dto.OrderGoodsStockQryDTO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;

import java.util.List;

public interface OrderGoodsStockServiceI {

    List<OrderGoodsStockQryDTO> listDTOByCondition(OrderGoodsStockQry orderGoodsStockPageQry);

    /**
     * 批量插入
     * @param createList
     */
    void batchCreate(List<OrderGoodsStockDO> createList);

    /**
     *
     * @param orderIdList
     * @return
     */
    List<OrderGoodsStockDO> listByOrderIdList(List<Integer> orderIdList);

    /**
     * 批量修改
     * @param updateGoodsStockList
     */
    void batchUpdate(List<OrderGoodsStockDO> updateGoodsStockList);
}
