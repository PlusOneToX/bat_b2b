package com.bat.order.service.stock.convertor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.bat.order.service.common.Constant;
import com.bat.order.service.common.enumtype.StockLockType;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.stock.dto.OrderErpNoLineNumberList;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemCountDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.dubboapi.warehouse.stock.dto.VminStockLockDTO;
import com.bat.dubboapi.warehouse.stock.dto.WarehouseInStockLockDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO2;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO2;
import com.bat.order.mq.dto.OrderErpNoLineDTO;

public class OrderGoodsStockConvertor {

    public static List<ItemStockLockDTO> toItemStockLockDTOList(List<OrderGoodsStockDO> orderGoodsStockDOS) {
        Map<Integer, ItemStockLockDTO> dtoMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderGoodsStockDOS)) {
            orderGoodsStockDOS.forEach(orderGoodsStockDO -> {
                ItemStockLockDTO stockLockDTO = dtoMap.get(orderGoodsStockDO.getItemId());
                if (stockLockDTO == null) {
                    stockLockDTO = new ItemStockLockDTO();
                    stockLockDTO.setItemId(orderGoodsStockDO.getItemId());
                    dtoMap.put(orderGoodsStockDO.getItemId(), stockLockDTO);
                }
                // 锁定类型 1.在库 2.在途 3.vmi
                if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_1)) {
                    List<WarehouseInStockLockDTO> inStockLockDTOList = stockLockDTO.getInStockLockDTOList();
                    if (inStockLockDTOList == null) {
                        inStockLockDTOList = new ArrayList<>();
                        stockLockDTO.setInStockLockDTOList(inStockLockDTOList);
                    }
                    WarehouseInStockLockDTO inStockLockDTO = new WarehouseInStockLockDTO();
                    inStockLockDTO.setWarehouseId(orderGoodsStockDO.getWarehouseId());
                    inStockLockDTO.setStockId(orderGoodsStockDO.getStockId());
                    inStockLockDTO.setNumInWarehouseLock(orderGoodsStockDO.getLockNum());
                    inStockLockDTOList.add(inStockLockDTO);
                } else if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_2)) {
                    List<WarehouseInStockLockDTO> inStockLockDTOList = stockLockDTO.getOnWayLockDTOList();
                    if (inStockLockDTOList == null) {
                        inStockLockDTOList = new ArrayList<>();
                        stockLockDTO.setOnWayLockDTOList(inStockLockDTOList);
                    }
                    WarehouseInStockLockDTO inStockLockDTO = new WarehouseInStockLockDTO();
                    inStockLockDTO.setWarehouseId(orderGoodsStockDO.getWarehouseId());
                    inStockLockDTO.setStockId(orderGoodsStockDO.getStockId());
                    inStockLockDTO.setNumOnWayLock(orderGoodsStockDO.getLockNum());
                    inStockLockDTOList.add(inStockLockDTO);
                } else {
                    VminStockLockDTO vmiLock = stockLockDTO.getVmiLock();
                    if (vmiLock == null) {
                        vmiLock = new VminStockLockDTO();
                        stockLockDTO.setVmiLock(vmiLock);
                        vmiLock.setWarehouseId(orderGoodsStockDO.getWarehouseId());
                        vmiLock.setStockId(orderGoodsStockDO.getStockId());
                        vmiLock.setLockNum(orderGoodsStockDO.getLockNum());
                    } else {
                        vmiLock.setLockNum(vmiLock.getLockNum() + orderGoodsStockDO.getLockNum());
                    }
                }
            });
        }
        return dtoMap.values().stream().collect(Collectors.toList());
    }

    public static List<OrderGoodsStockDO> toOrderGoodsStockDOList(
        Map<OrderGoodsDO, GoodsItemCountDTO> goodsItemCountDTOMap, List<ItemStockLockDTO> addLockDTOS) {
        Date time = new Date(System.currentTimeMillis());
        List<OrderGoodsStockDO> stockDOS = new ArrayList<>();
        Map<Integer, ItemStockLockDTO> addLockDTOMap =
            addLockDTOS.stream().collect(Collectors.toMap(ItemStockLockDTO::getItemId, addLockDTO -> addLockDTO));
        goodsItemCountDTOMap.forEach((k, v) -> {
            ItemStockLockDTO stockLockDTO = addLockDTOMap.get(k.getItemId());
            // vmi锁定
            VminStockLockDTO vmiLock = stockLockDTO.getVmiLock();
            // 在库锁定(订单变更，增加库存情况只有在库锁定数量，如果锁定数量大于在库数量，属于超卖情况)
            List<WarehouseInStockLockDTO> inStockLockDTOList = stockLockDTO.getInStockLockDTOList();
            List<WarehouseInStockLockDTO> onWayLockDTOList = stockLockDTO.getOnWayLockDTOList();
            if (CollectionUtils.isEmpty(inStockLockDTOList) && CollectionUtils.isEmpty(onWayLockDTOList)) {
                throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_GOODS_IN_STOCK_NULL,
                    "\"" + k.getItemCode() + "\"" + MessageUtils.get(OrderInfoErrorCode.B_ORDER_GOODS_IN_STOCK_NULL));
            }
            AtomicInteger itemCount = new AtomicInteger(v.getInStockCount() + v.getOnWayCount());
            // 在库
            if (!CollectionUtils.isEmpty(inStockLockDTOList)) {
                inStockLockDTOList.forEach(stock -> {
                    if (itemCount.get() > 0 && stock.getNumInWarehouseLock() != null
                        && stock.getNumInWarehouseLock().intValue() > 0) {
                        OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                        orderGoodsStock.setOrderId(k.getOrderId());
                        orderGoodsStock.setOrderGoodsId(k.getId());
                        orderGoodsStock.setLockType(StockLockType.LOCK_IN.getValue());
                        orderGoodsStock.setGoodsId(k.getGoodsId());
                        orderGoodsStock.setItemId(k.getItemId());
                        orderGoodsStock.setStockId(stock.getStockId());
                        orderGoodsStock.setWarehouseId(stock.getWarehouseId());
                        orderGoodsStock.setCreateTime(time);
                        orderGoodsStock.setUpdateTime(time);
                        stockDOS.add(orderGoodsStock);
                        // 锁库数量计算
                        if (stock.getNumInWarehouseLock() > itemCount.get()) {
                            orderGoodsStock.setLockNum(itemCount.get());
                            stock.setNumInWarehouseLock(stock.getNumInWarehouseLock() - itemCount.get());
                            itemCount.set(0);
                        } else {
                            orderGoodsStock.setLockNum(stock.getNumInWarehouseLock());
                            stock.setNumInWarehouseLock(0);
                            itemCount.set(itemCount.get() - stock.getNumInWarehouseLock());
                        }
                    }
                });
            }
            // 在途
            if (!CollectionUtils.isEmpty(onWayLockDTOList)) {
                onWayLockDTOList.forEach(onWayLockDTO -> {
                    if (itemCount.get() > 0 && onWayLockDTO.getNumOnWayLock() != null
                        && onWayLockDTO.getNumOnWayLock().intValue() > 0) {
                        OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                        orderGoodsStock.setOrderId(k.getOrderId());
                        orderGoodsStock.setOrderGoodsId(k.getId());
                        orderGoodsStock.setLockType(StockLockType.LOCK_ON_WAY.getValue());
                        orderGoodsStock.setGoodsId(k.getGoodsId());
                        orderGoodsStock.setItemId(k.getItemId());
                        orderGoodsStock.setStockId(onWayLockDTO.getStockId());
                        orderGoodsStock.setWarehouseId(onWayLockDTO.getWarehouseId());
                        orderGoodsStock.setCreateTime(time);
                        orderGoodsStock.setUpdateTime(time);
                        stockDOS.add(orderGoodsStock);
                        // 锁库数量计算
                        if (onWayLockDTO.getNumOnWayLock() > itemCount.get()) {
                            orderGoodsStock.setLockNum(itemCount.get());
                            onWayLockDTO.setNumOnWayLock(onWayLockDTO.getNumOnWayLock() - itemCount.get());
                            itemCount.set(0);
                        } else {
                            orderGoodsStock.setLockNum(onWayLockDTO.getNumOnWayLock());
                            onWayLockDTO.setNumOnWayLock(0);
                            itemCount.set(itemCount.get() - onWayLockDTO.getNumOnWayLock());
                        }
                    }
                });
            }
            // VMI库存
            if (itemCount.get() > 0 && vmiLock != null && vmiLock.getLockNum() > 0) {
                OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                orderGoodsStock.setOrderId(k.getOrderId());
                orderGoodsStock.setOrderGoodsId(k.getId());
                orderGoodsStock.setLockType(StockLockType.LOCK_VMI.getValue());
                orderGoodsStock.setGoodsId(k.getGoodsId());
                orderGoodsStock.setItemId(k.getItemId());
                orderGoodsStock.setStockId(vmiLock.getStockId());
                orderGoodsStock.setWarehouseId(vmiLock.getWarehouseId());
                orderGoodsStock.setCreateTime(time);
                orderGoodsStock.setUpdateTime(time);
                stockDOS.add(orderGoodsStock);
                // 锁库数量计算
                if (vmiLock.getLockNum() > itemCount.get()) {
                    orderGoodsStock.setLockNum(itemCount.get());
                    vmiLock.setLockNum(vmiLock.getLockNum() - itemCount.get());
                    itemCount.set(0);
                } else {
                    orderGoodsStock.setLockNum(vmiLock.getLockNum());
                    vmiLock.setLockNum(0);
                    itemCount.set(itemCount.get() - vmiLock.getLockNum());
                }
                orderGoodsStock.setLockNum(vmiLock.getLockNum());
            }
        });
        return stockDOS;
    }

    public static OrderGoodsStockDO getOrderGoodsStockDOBaseMsg(OrderGoodsDO orderGoodsDO, Short lockType,
        Integer lockNum) {
        OrderGoodsStockDO inStockDO = new OrderGoodsStockDO();
        inStockDO.setOrderId(orderGoodsDO.getOrderId());
        inStockDO.setOrderGoodsId(orderGoodsDO.getId());
        inStockDO.setGoodsId(orderGoodsDO.getGoodsId());
        inStockDO.setItemId(orderGoodsDO.getItemId());
        inStockDO.setCreateTime(new Date());
        inStockDO.setUpdateTime(new Date());
        inStockDO.setLockType(lockType);
        inStockDO.setLockNum(lockNum);
        inStockDO.setWarehouseId(orderGoodsDO.getWarehouseId());
        return inStockDO;
    }

    public static Map<Integer, List<ItemStockLockDTO>> toOrderUnLockStock(List<OrderErpNoLineDTO> orderErpNoLineDTOS,
        List<OrderGoodsStockDO2> orderGoodsStockDO2S, List<Integer> deleteIds,
        List<OrderGoodsStockDO2> changeOrderGoodsStockDOS, List<OrderErpNoLineDTO> orderErpNoLineDTOS2) {
        Map<Integer, List<ItemStockLockDTO>> unOrderLockStockMap = new HashMap<>();
        Map<Integer, ItemStockLockDTO> unLockStockMap = new HashMap<>();
        Date date = new Date();
        Map<String, List<OrderGoodsStockDO2>> orderGoodsStockDO2SMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(orderGoodsStockDO2S)) {
            orderGoodsStockDO2SMap
                .putAll(orderGoodsStockDO2S.stream().collect(Collectors.groupingBy(OrderGoodsStockDO2::getOrderErpNo)));
        }
        orderErpNoLineDTOS.forEach(lineNumber -> {
            // <ERP订单行序号，行数量>
            Map<Integer, Integer> lineNumberMap = lineNumber.getLineNumberMap();
            List<OrderGoodsStockDO2> stockDO2s = orderGoodsStockDO2SMap.get(lineNumber.getOrderErpNo());
            Map<Integer, List<OrderGoodsStockDO2>> stockDO2SMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(stockDO2s)) {
                stockDO2SMap
                    .putAll(stockDO2s.stream().collect(Collectors.groupingBy(OrderGoodsStockDO2::getSerialNumber)));
            }
            lineNumberMap.forEach((k, v) -> {
                if (v < 0) {// 订单解锁库存
                    List<OrderGoodsStockDO2> goodsStockDO2s = stockDO2SMap.get(k);
                    if (!CollectionUtils.isEmpty(goodsStockDO2s)) {
                        // 注意要做下绝对值
                        AtomicReference<Integer> unLockNum = new AtomicReference<>(Math.abs(v));
                        // 可能一个行项目多个锁库记录情况
                        goodsStockDO2s.forEach(stockDO2 -> {
                            if (unLockNum.get() == 0) {
                                return;
                            }
                            Integer lockNum = 0;
                            // 当解锁数量小于锁定数量时，更新订单明对应行项目锁库，否则删除订单明对应行项目锁库
                            if (unLockNum.get() >= stockDO2.getLockNum()) {// 锁库记录够扣情况
                                deleteIds.add(stockDO2.getId());
                                lockNum = stockDO2.getLockNum();
                                unLockNum.set(unLockNum.get() - stockDO2.getLockNum());
                            } else { // 锁库记录不够扣情况
                                lockNum = unLockNum.get();
                                stockDO2.setLockNum(stockDO2.getLockNum() - unLockNum.get());
                                stockDO2.setUpdateTime(date);
                                if (!changeOrderGoodsStockDOS.contains(stockDO2)) {
                                    changeOrderGoodsStockDOS.add(stockDO2);
                                }
                                unLockNum.set(0);
                            }
                            // 库存解锁
                            List<ItemStockLockDTO> unLockDTOS = unOrderLockStockMap.get(stockDO2.getOrderId());
                            if (unLockDTOS == null) {
                                unLockDTOS = new ArrayList<>();
                                unOrderLockStockMap.put(stockDO2.getOrderId(), unLockDTOS);
                            }
                            ItemStockLockDTO lockDTO = unLockStockMap.get(stockDO2.getItemId());
                            if (lockDTO == null) {
                                lockDTO = new ItemStockLockDTO();
                                lockDTO.setItemId(stockDO2.getItemId());
                                unLockStockMap.put(stockDO2.getItemId(), lockDTO);
                                unLockDTOS.add(lockDTO);
                            }
                            // 锁定类型 1.在库 2.在途 3.vmi
                            if (stockDO2.getLockType().equals(Constant.LOCK_TYPE_1)) {
                                List<WarehouseInStockLockDTO> inStockLockDTOList = lockDTO.getInStockLockDTOList();
                                if (inStockLockDTOList == null) {
                                    inStockLockDTOList = new ArrayList<>();
                                    lockDTO.setInStockLockDTOList(inStockLockDTOList);
                                }
                                WarehouseInStockLockDTO inStockLockDTO = new WarehouseInStockLockDTO();
                                inStockLockDTOList.add(inStockLockDTO);
                                inStockLockDTO.setStockId(stockDO2.getStockId());
                                inStockLockDTO.setWarehouseId(stockDO2.getWarehouseId());
                                inStockLockDTO.setNumInWarehouseLock(lockNum);
                            } else if (stockDO2.getLockType().equals(Constant.LOCK_TYPE_2)) {
                                List<WarehouseInStockLockDTO> onWayLockDTOList = lockDTO.getOnWayLockDTOList();
                                if (onWayLockDTOList == null) {
                                    onWayLockDTOList = new ArrayList<>();
                                    lockDTO.setOnWayLockDTOList(onWayLockDTOList);
                                }
                                WarehouseInStockLockDTO onWayLockDTO = new WarehouseInStockLockDTO();
                                onWayLockDTOList.add(onWayLockDTO);
                                onWayLockDTO.setStockId(stockDO2.getStockId());
                                onWayLockDTO.setWarehouseId(stockDO2.getWarehouseId());
                                onWayLockDTO.setNumOnWayLock(lockNum);
                            } else if (stockDO2.getLockType().equals(Constant.LOCK_TYPE_3)) {
                                VminStockLockDTO vmiLock = lockDTO.getVmiLock();
                                if (vmiLock == null) {
                                    vmiLock = new VminStockLockDTO();
                                    lockDTO.setVmiLock(vmiLock);
                                }
                                vmiLock.setStockId(stockDO2.getStockId());
                                vmiLock.setWarehouseId(stockDO2.getWarehouseId());
                                vmiLock.setLockNum(lockNum);
                            }
                        });
                    }
                } else {// 订单加锁
                    if (!orderErpNoLineDTOS2.contains(lineNumber)) {
                        orderErpNoLineDTOS2.add(lineNumber);
                    }
                }
            });
        });
        return unOrderLockStockMap;
    }

    public static List<GoodsItemCountDTO> toOrderLockStock(List<OrderErpNoLineDTO> orderErpNoLineDTOS2,
        List<OrderGoodsDO2> orderGoodsDO2S, List<OrderGoodsStockDO> addOrderGoodsStockDOS) {
        List<GoodsItemCountDTO> itemCountDTOS = new ArrayList<>();
        Map<String, List<OrderGoodsDO2>> OrderGoodsDO2SMap =
            orderGoodsDO2S.stream().collect(Collectors.groupingBy(OrderGoodsDO2::getOrderErpNo));
        Date date = new Date();
        orderErpNoLineDTOS2.forEach(lineNumber -> {
            List<OrderGoodsDO2> orderGoodss = OrderGoodsDO2SMap.get(lineNumber.getOrderErpNo());
            if (!CollectionUtils.isEmpty(orderGoodss)) {
                // <ERP订单行序号，行数量>
                Map<Integer, Integer> lineNumberMap = lineNumber.getLineNumberMap();
                lineNumberMap.forEach((k, v) -> {
                    if (v > 0) {// 订单加锁
                        Optional<OrderGoodsDO2> first = orderGoodss.stream()
                            .filter(orderGoods -> orderGoods.getSerialNumber().equals(k)).findFirst();
                        if (first != null && first.isPresent()) {
                            OrderGoodsDO2 orderGoodsDO2 = first.get();
                            GoodsItemCountDTO itemCountDTO = new GoodsItemCountDTO();
                            itemCountDTO.setItemId(orderGoodsDO2.getItemId());
                            itemCountDTO.setSupportOversoldFlag(true);
                            itemCountDTO.setInStockCount(Math.abs(v));
                            itemCountDTO.setOnWayCount(0);
                            itemCountDTOS.add(itemCountDTO);
                            // 锁库明细
                            OrderGoodsStockDO orderGoodsStockDO = new OrderGoodsStockDO();
                            addOrderGoodsStockDOS.add(orderGoodsStockDO);
                            orderGoodsStockDO.setLockNum(v);
                            orderGoodsStockDO.setOrderId(orderGoodsDO2.getOrderId());
                            orderGoodsStockDO.setOrderGoodsId(orderGoodsDO2.getId());
                            orderGoodsStockDO.setGoodsId(orderGoodsDO2.getGoodsId());
                            orderGoodsStockDO.setItemId(orderGoodsDO2.getItemId());
                            orderGoodsStockDO.setWarehouseId(orderGoodsDO2.getWarehouseId());
                            orderGoodsStockDO.setLockType(Constant.LOCK_TYPE_1);
                            orderGoodsStockDO.setCreateTime(date);
                            orderGoodsStockDO.setUpdateTime(date);
                        }
                    }
                });
            }
        });
        return itemCountDTOS;
    }

    public static List<OrderErpNoLineDTO> toOrderErpNoLineDTOS(List<OrderErpNoLineNumberList> lineNumberLists) {
        List<OrderErpNoLineDTO> orderErpNoLineDTOS = new ArrayList<>();
        lineNumberLists.forEach(lineNumber -> {
            OrderErpNoLineDTO orderErpNoLineDTO = new OrderErpNoLineDTO();
            BeanUtils.copyProperties(lineNumber, orderErpNoLineDTO);
            orderErpNoLineDTOS.add(orderErpNoLineDTO);
        });
        return orderErpNoLineDTOS;
    }

    public static void toOrderGoodsStockDOS(List<OrderGoodsStockDO> addOrderGoodsStockDOS,
        List<ItemStockLockDTO> stockLockDTOS) {
        if (!CollectionUtils.isEmpty(stockLockDTOS)) {
            addOrderGoodsStockDOS.forEach(addOrderGoodsStockDO -> {
                ItemStockLockDTO lockDTO = stockLockDTOS.stream()
                    .filter(stockLockDTO -> stockLockDTO.getItemId().equals(addOrderGoodsStockDO.getItemId()))
                    .findFirst().orElse(null);
                if (lockDTO != null) {
                    VminStockLockDTO vmiLock = lockDTO.getVmiLock();
                    if (vmiLock != null) {
                        addOrderGoodsStockDO.setStockId(vmiLock.getStockId());
                    }
                    if (!CollectionUtils.isEmpty(lockDTO.getOnWayLockDTOList())) {
                        addOrderGoodsStockDO.setStockId(lockDTO.getOnWayLockDTOList().get(0).getStockId());
                    }
                    if (!CollectionUtils.isEmpty(lockDTO.getInStockLockDTOList())) {
                        addOrderGoodsStockDO.setStockId(lockDTO.getInStockLockDTOList().get(0).getStockId());
                    }
                }
            });
        }

    }

}
