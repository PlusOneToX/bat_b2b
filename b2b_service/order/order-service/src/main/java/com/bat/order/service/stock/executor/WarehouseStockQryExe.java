package com.bat.order.service.stock.executor;

import java.util.*;
import java.util.stream.Collectors;

import com.bat.order.service.common.Constant;
import com.bat.order.service.common.error.DubboServiceErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.warehouse.common.Response;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.GoodsVmiStockRpcDTO;
import com.bat.dubboapi.warehouse.stock.dto.WarehouseInStockRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.deliver.OrderDeliverBillDetailServiceI;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.api.stock.api.OrderGoodsStockServiceI;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDetailDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.service.stock.convertor.OrderGoodsStockConvertor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WarehouseStockQryExe {

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private WarehouseStockServiceRpc warehouseStockServiceRpc;

    @Autowired
    private OrderGoodsStockServiceI orderGoodsStockServiceI;

    @Autowired
    private OrderGoodsServiceI orderGoodsServiceI;

    @Autowired
    private OrderDeliverBillDetailServiceI orderDeliverBillDetailServiceI;

    /**
     * 根据仓库id和货品id查询在库库存
     * 
     * @param warehouseId
     * @param itemId
     */
    public WarehouseInStockRpcDTO queryInStock(Integer warehouseId, Integer itemId) {
        Response<WarehouseInStockRpcDTO> response =
            warehouseStockServiceRpc.getByWarehouseIdAndItemId(warehouseId, itemId);
        if (response == null) {
            throw OrderException.buildException(DubboServiceErrorCode.DUBBO_WAREHOUSE_SERVICE_EXCEPTION);
        }
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return response.getData();
    }

    /**
     * 根据货品id查询 vmi库存
     * 
     * @param itemId
     */
    public GoodsVmiStockRpcDTO queryVmiStock(Integer itemId) {
        Response<GoodsVmiStockRpcDTO> response = warehouseStockServiceRpc.getVmiByItemId(itemId);
        if (response == null) {
            throw OrderException.buildException(DubboServiceErrorCode.DUBBO_WAREHOUSE_SERVICE_EXCEPTION);
        }
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return response.getData();
    }

    // /**
    // * 根据货品id查询在途库存
    // *
    // * @param itemId
    // */
    // public GoodsOnWayStockRpcDTO queryOnWayStock(Integer itemId) {
    // Response<GoodsOnWayStockRpcDTO> response = warehouseStockServiceRpc.getOnWayByItemId(itemId);
    // if (response == null) {
    // throw OrderException.buildException(DubboServiceErrorCode.DUBBO_WAREHOUSE_SERVICE_EXCEPTION);
    // }
    // if (!response.isSuccess()) {
    // throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
    // }
    // return response.getData();
    // }

    /**
     * 出库单状态取消或者删除之后（不处理锁库记录，有库存变更环节处理）
     * 
     * @param orderGoodsMap
     * @param deliverGoodsDetails
     */
    public void createOrderGoodsStockAgainByOutboundOrderStatus(Map<Integer, OrderGoodsDO> orderGoodsMap,
        List<OrderDeliverBillDetailDO> deliverGoodsDetails, List<OrderGoodsDO> changeOrderGoods) {
        // 所有的订单id列表
        deliverGoodsDetails.forEach(billDetailDO -> {
            OrderGoodsDO orderGoodsDO = orderGoodsMap.get(billDetailDO.getOrderGoodsId());
            if (orderGoodsDO != null) {
                // 出库的数量
                Integer count = billDetailDO.getCount();
                if (orderGoodsDO.getDeliverCount() != null) {
                    int deliverCount = orderGoodsDO.getDeliverCount() - count;
                    orderGoodsDO.setDeliverCount(deliverCount);
                    if (deliverCount < 0) {
                        orderGoodsDO.setDeliverCount(0);
                    }
                } else {
                    orderGoodsDO.setDeliverCount(0);
                }
                orderGoodsDO.setUnDeliverCount(orderGoodsDO.getItemCount() - orderGoodsDO.getDeliverCount());
                changeOrderGoods.add(orderGoodsDO);
            }
        });
    }

    private OrderGoodsStockDO getOrderGoodsStock(Integer orderId, OrderGoodsDO orderGoodsDO, Integer stockId,
        Short lockType, Integer lockNum) {
        OrderGoodsStockDO orderGoodsStockDO = new OrderGoodsStockDO();
        orderGoodsStockDO.setOrderId(orderId);
        orderGoodsStockDO.setOrderGoodsId(orderGoodsDO.getId());
        orderGoodsStockDO.setGoodsId(orderGoodsDO.getGoodsId());
        orderGoodsStockDO.setItemId(orderGoodsDO.getItemId());
        orderGoodsStockDO.setStockId(stockId);
        // 记录的是原因下单的仓库id
        orderGoodsStockDO.setWarehouseId(orderGoodsDO.getWarehouseId());
        orderGoodsStockDO.setLockNum(lockNum);
        orderGoodsStockDO.setLockType(lockType);
        orderGoodsStockDO.setCreateTime(new Date());
        orderGoodsStockDO.setUpdateTime(new Date());
        return orderGoodsStockDO;
    }

    public void dealWithOrderGoodsStockBySyncOutboundAgain(List<Integer> orderIds) {
        log.info("之前同步过出库单、数量变少、需要锁上或者增加 orderIds:{}", orderIds);
        // 所有订单涉及的order_goods(用于处理order_goods_stock)
        List<OrderGoodsDO> allOrderGoodsList = orderGoodsServiceI.listByOrderIdList(orderIds);
        List<OrderGoodsStockDO> orderGoodsStockDOList = orderGoodsStockServiceI.listByOrderIdList(orderIds);
        Map<String, OrderGoodsStockDO> map = new HashMap<>();
        orderGoodsStockDOList.forEach(orderGoodsStock -> {
            map.put(orderGoodsStock.getOrderGoodsId() + "_" + orderGoodsStock.getLockType(), orderGoodsStock);
        });
        // 需要加回来的order_goods_stock明细列表
        List<OrderGoodsStockDO> addList = new ArrayList<>();
        // 需要增加锁库数量的（之前发5个、后面改为发3个、需要加2个锁库明细、减少锁库数量在库存变更回调处理（因为不知道减少是订单明细还是货品入库（非订单）））
        List<OrderGoodsStockDO> updateList = new ArrayList<>();

        /* List<OrderDeliverBillDetailDO> orderDeliverBillDetailDOList = orderDeliverBillDetailServiceI.listByOrderIdList(orderIds);
        Map<Integer, List<OrderDeliverBillDetailDO>> billDetailMap = new HashMap<>();
        if(orderDeliverBillDetailDOList !=null && orderDeliverBillDetailDOList.size()>0){
            billDetailMap = orderDeliverBillDetailDOList.stream().collect(Collectors.toMap(OrderDeliverBillDetailDO::getOrderGoodsId, orderDeliverBillDetailDO ->
                    Lists.newArrayList(orderDeliverBillDetailDO),
                    (List<OrderDeliverBillDetailDO> oldList,List<OrderDeliverBillDetailDO> newList)->{
                        oldList.addAll(newList);
                        return oldList;
                    }));
        }*/
        for (OrderGoodsDO orderGoodsDO : allOrderGoodsList) {
            // 未发数量（也就是需要锁定的数量）
            Integer lockNum = orderGoodsDO.getUnDeliverCount();
            // 找到明细的锁库明细列表（先反锁在途、再VMI、最后在库）
            if (orderGoodsDO.getItemOnWayCount() != null && orderGoodsDO.getItemOnWayCount() > 0) {
                // 先锁在途
                Integer num = lockNum < orderGoodsDO.getItemOnWayCount() ? lockNum : orderGoodsDO.getItemOnWayCount();
                dealwithOrderGoodsStock(map, addList, updateList, orderGoodsDO, num, Constant.LOCK_TYPE_2);
                // 剩余需要反锁的数量
                lockNum -= num;
            }
            if (orderGoodsDO.getItemVmiCount() != null && orderGoodsDO.getItemVmiCount() > 0) {
                Integer num = lockNum < orderGoodsDO.getItemVmiCount() ? lockNum : orderGoodsDO.getItemVmiCount();

                dealwithOrderGoodsStock(map, addList, updateList, orderGoodsDO, num, Constant.LOCK_TYPE_3);
                // 剩余需要反锁的数量
                lockNum -= num;
            }
            if (orderGoodsDO.getItemInCount() != null && orderGoodsDO.getItemInCount() > 0) {

                dealwithOrderGoodsStock(map, addList, updateList, orderGoodsDO, lockNum, Constant.LOCK_TYPE_1);

            }

        }
        // 处理添加的列表的stockId
        if (addList.size() > 0) {
            // 设置stockId
            setStockId(addList);
            orderGoodsStockServiceI.batchCreate(addList);
        }
        if (updateList.size() > 0) {
            orderGoodsStockServiceI.batchUpdate(updateList);
        }
    }

    private void setStockId(List<OrderGoodsStockDO> addList) {
        Set<String> instockKeySet = new HashSet<>();
        Set<Integer> vmiStockItemIdSet = new HashSet<>();
        Set<String> onWayItemIdSet = new HashSet<>();
        Map<String, WarehouseInStockRpcDTO> inStockRpcDTOMap = new HashMap<>();
        Map<Integer, GoodsVmiStockRpcDTO> vmiStockRpcDTOMap = new HashMap<>();
        Map<String, WarehouseInStockRpcDTO> onWayStockRpcDTOMap = new HashMap<>();
        addList.forEach(orderGoodsStockDO -> {
            if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_1)) {
                instockKeySet.add(orderGoodsStockDO.getWarehouseId() + "_" + orderGoodsStockDO.getItemId());
            }
            if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_2)) {
                onWayItemIdSet.add(orderGoodsStockDO.getWarehouseId() + "_" + orderGoodsStockDO.getItemId());
            }
            if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_3)) {
                vmiStockItemIdSet.add(orderGoodsStockDO.getItemId());
            }
        });
        if (instockKeySet.size() > 0) {
            // 查询在库库存
            Response<List<WarehouseInStockRpcDTO>> response =
                warehouseStockServiceRpc.listInstockByRedisKeySet(instockKeySet);
            if (!response.isSuccess()) {
                throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
            }
            List<WarehouseInStockRpcDTO> inStockRpcDTOList = response.getData();
            inStockRpcDTOList.forEach(warehouseInStockRpcDTO -> inStockRpcDTOMap.put(
                warehouseInStockRpcDTO.getWarehouseId() + "_" + warehouseInStockRpcDTO.getItemId(),
                warehouseInStockRpcDTO));
        }
        if (vmiStockItemIdSet.size() > 0) {
            Response<List<GoodsVmiStockRpcDTO>> response =
                warehouseStockServiceRpc.listVmiByItemIdList(vmiStockItemIdSet);
            if (!response.isSuccess()) {
                throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
            }
            vmiStockRpcDTOMap = response.getData().stream()
                .collect(Collectors.toMap(GoodsVmiStockRpcDTO::getItemId, goodsVmiStockRpcDTO -> goodsVmiStockRpcDTO));
        }
        if (onWayItemIdSet.size() > 0) {
            Response<List<WarehouseInStockRpcDTO>> response =
                warehouseStockServiceRpc.listInstockByRedisKeySet(onWayItemIdSet);
            if (!response.isSuccess()) {
                throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
            }
            List<WarehouseInStockRpcDTO> onWayRpcDTOList = response.getData();
            onWayRpcDTOList.forEach(warehouseInStockRpcDTO -> inStockRpcDTOMap.put(
                warehouseInStockRpcDTO.getWarehouseId() + "_" + warehouseInStockRpcDTO.getItemId(),
                warehouseInStockRpcDTO));
        }
        // if (onWayItemIdSet.size() > 0) {
        // Response<List<GoodsOnWayStockRpcDTO>> response =
        // warehouseStockServiceRpc.listOnWayByItemIdList(onWayItemIdSet);
        // if (!response.isSuccess()) {
        // throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        // }
        // onWayStockRpcDTOMap = response.getData().stream().collect(
        // Collectors.toMap(GoodsOnWayStockRpcDTO::getItemId, goodsOnWayStockRpcDTO -> goodsOnWayStockRpcDTO));
        // }
        for (int x = 0; x < addList.size(); x++) {
            OrderGoodsStockDO orderGoodsStockDO = addList.get(x);
            if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_1)) {
                WarehouseInStockRpcDTO warehouseInStockRpcDTO =
                    inStockRpcDTOMap.get(orderGoodsStockDO.getWarehouseId() + "_" + orderGoodsStockDO.getItemId());
                if (warehouseInStockRpcDTO != null) {
                    orderGoodsStockDO.setStockId(warehouseInStockRpcDTO.getId());
                }
            }
            if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_2)) {
                WarehouseInStockRpcDTO onWayRpcDTO =
                    inStockRpcDTOMap.get(orderGoodsStockDO.getWarehouseId() + "_" + orderGoodsStockDO.getItemId());
                if (onWayRpcDTO != null) {
                    orderGoodsStockDO.setStockId(onWayRpcDTO.getId());
                }
            }
            if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_3)) {
                GoodsVmiStockRpcDTO goodsVmiStockRpcDTO = vmiStockRpcDTOMap.get(orderGoodsStockDO.getItemId());
                if (goodsVmiStockRpcDTO != null) {
                    orderGoodsStockDO.setStockId(goodsVmiStockRpcDTO.getId());
                }
            }
        }
    }

    private void dealwithOrderGoodsStock(Map<String, OrderGoodsStockDO> map, List<OrderGoodsStockDO> addList,
        List<OrderGoodsStockDO> updateList, OrderGoodsDO orderGoodsDO, Integer lockNum, Short lockType) {
        if (lockNum == null || lockNum == 0) {
            return;
        }
        // 含有在库
        OrderGoodsStockDO orderGoodsStockDO = map.get(orderGoodsDO.getId() + "_" + lockType);
        if (orderGoodsStockDO == null) {
            // 之前发货、现在
            OrderGoodsStockDO inStockDO =
                OrderGoodsStockConvertor.getOrderGoodsStockDOBaseMsg(orderGoodsDO, lockType, lockNum);
            // 设置stock的主键
            addList.add(inStockDO);
            return;
        }
        // 修改(锁库数量增加才放、减少的在库存变更已做处理)
        if (orderGoodsStockDO.getLockNum() < lockNum) {
            // 矫正数量
            orderGoodsStockDO.setLockNum(lockNum);
            orderGoodsStockDO.setUpdateTime(new Date());
            updateList.add(orderGoodsStockDO);
        }
    }

}
