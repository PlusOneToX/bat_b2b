package com.bat.dubboapi.order.stock.api;

import com.bat.dubboapi.order.stock.dto.OrderErpNoLineNumberIdList;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.stock.dto.OrderErpNoLineNumberList;
import com.bat.dubboapi.order.stock.dto.OrderGoodsStockSimpleDTO;

import java.util.List;

public interface OrderGoodsStockServiceRpc {


    /**
     * 根据ERP订单号和行序号列表查询B2B订单锁库列表
     */
    Response<List<OrderGoodsStockSimpleDTO>> listByOrderErpNoAndSerialNumber(List<OrderErpNoLineNumberIdList> lineNumberIdLists);


    /**
     * 根据ERP订单号和行序号列表解锁B2B订单
     */
    Response unLockOrderErpNoAndSerialNumber(List<OrderErpNoLineNumberList> lineNumberLists);

    /**
     * 根据仓库id查询在库/VMI锁库
     *
     * @param warehousesId
     * @param lockType     1、在库 2、在途 3、VMI
     * @return
     */
    Response<List<OrderGoodsStockSimpleDTO>> sumByWarehouseIdAndLockTypeGroupByItemId(Integer warehousesId, Short lockType, Integer itemId);


    /**
     * 根据货品ids 查询在库/在途/VMI 锁库记录
     *
     * @return
     */
    Response<List<OrderGoodsStockSimpleDTO>> sumByItemIds(List<Integer> itemIds);
}
