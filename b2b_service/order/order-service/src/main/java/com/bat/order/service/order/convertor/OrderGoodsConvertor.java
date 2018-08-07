package com.bat.order.service.order.convertor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.bat.order.service.common.Constant;
import com.bat.order.service.common.enumtype.StockLockType;
import com.bat.order.service.common.error.OrderGoodsErrorCode;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.order.order.dto.ErpOrderChangeDetailCmd;
import com.bat.dubboapi.order.order.dto.erp.ErpOrderDetailEntryId;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemCountDTO;
import com.bat.dubboapi.warehouse.stock.dto.ItemStockLockDTO;
import com.bat.dubboapi.warehouse.stock.dto.VminStockLockDTO;
import com.bat.dubboapi.warehouse.stock.dto.WarehouseInStockLockDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.order.dto.orderquery.common.OrderGoodsDTO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.mq.dto.GoodsSaleDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/24 23:35
 */
@Slf4j
public class OrderGoodsConvertor {
    public static List<OrderGoodsDTO> toAddOrderGoodsDTOList(List<OrderGoodsDO> orderGoodsDOS) {
        if (!CollectionUtils.isEmpty(orderGoodsDOS)) {
            return orderGoodsDOS.stream().map(orderGoodsDO -> {
                OrderGoodsDTO dto = new OrderGoodsDTO();
                BeanUtils.copyProperties(orderGoodsDO, dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static OrderGoodsDTO toOrderGoodsDTO(OrderGoodsDO orderGoodsDO) {
        if (orderGoodsDO != null) {
            OrderGoodsDTO dto = new OrderGoodsDTO();
            BeanUtils.copyProperties(orderGoodsDO, dto);
            return dto;
        }
        return null;
    }

    /**
     * 订单变更 增加行项
     * 
     * @param addOrderGoodss
     * @param goodsItemRpcDTOMap
     * @param erpDistributorData
     * @param addLockDTOS
     * @param changeMap
     * @param saleDTOS
     * @return
     */
    public static List<OrderGoodsDO> toAddOrderGoodsDTOList(List<ErpOrderChangeDetailCmd> addOrderGoodss,
        Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap, OrderDistributorDataDO erpDistributorData,
        List<ItemStockLockDTO> addLockDTOS, Map<String, List<Object>> changeMap) {
        List<OrderGoodsDO> orderGoodsDOS = new ArrayList<>();
        Map<Integer, ItemStockLockDTO> addLockDTOMap =
            addLockDTOS.stream().collect(Collectors.toMap(ItemStockLockDTO::getItemId, addLockDTO -> addLockDTO));
        addOrderGoodss.forEach(addOrderGoods -> {
            OrderGoodsDO orderGoodsDO = new OrderGoodsDO();
            GoodsItemRpcDTO rpcDTO = goodsItemRpcDTOMap.get(addOrderGoods.getItemNo());
            BeanUtils.copyProperties(rpcDTO, orderGoodsDO);
            orderGoodsDO.setId(null);
            orderGoodsDO.setItemId(rpcDTO.getId());
            orderGoodsDO.setSerialNumber(addOrderGoods.getItemOrderId());
            orderGoodsDO.setItemCount(addOrderGoods.getNum());
            orderGoodsDO.setOrderId(erpDistributorData.getOrderId());
            if (StringUtils.isNotBlank(rpcDTO.getItemImg())) {
                orderGoodsDO.setImageUrl(rpcDTO.getItemImg());
            } else {
                orderGoodsDO.setImageUrl(rpcDTO.getImageUrl1());
            }
            if (addOrderGoods.getIsFree()) {
                orderGoodsDO.setItemType(Constant.ITEM_TYPE_2);
            } else {
                orderGoodsDO.setItemType(Constant.ITEM_TYPE_1);
            }
            orderGoodsDO.setItemInCount(orderGoodsDO.getItemCount());
            orderGoodsDO.setItemVmiCount(0);
            orderGoodsDO.setItemOnWayCount(0);
            orderGoodsDO.setDeliverCount(0);
            orderGoodsDO.setUnDeliverCount(orderGoodsDO.getItemCount());
            Date time = new Date(System.currentTimeMillis());
            orderGoodsDO.setCreateTime(time);
            orderGoodsDO.setUpdateTime(time);
            orderGoodsDOS.add(orderGoodsDO);
            // 添加订单明细锁库信息
            ItemStockLockDTO stockLockDTO = addLockDTOMap.get(orderGoodsDO.getItemId());
            if (stockLockDTO != null) {
                // vmi锁定
                VminStockLockDTO vmiLock = stockLockDTO.getVmiLock();
                // 在库锁定
                List<WarehouseInStockLockDTO> stocks = stockLockDTO.getInStockLockDTOList();
                if (CollectionUtils.isEmpty(stocks)) {
                    throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_GOODS_IN_STOCK_NULL,
                        "\"" + orderGoodsDO.getItemCode() + "\"" + MessageUtils.get(OrderInfoErrorCode.B_ORDER_GOODS_IN_STOCK_NULL));
                }
                List<OrderGoodsStockDO> stockDOS = new ArrayList<>();
                AtomicInteger itemCount = new AtomicInteger(orderGoodsDO.getItemCount());
                stocks.forEach(stock -> {
                    if (itemCount.get() > 0 && stock.getNumInWarehouseLock() != null
                        && stock.getNumInWarehouseLock().intValue() > 0) {
                        OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                        orderGoodsStock.setLockType(StockLockType.LOCK_IN.getValue());
                        if (stock.getNumInWarehouseLock().intValue() > itemCount.get()) {
                            orderGoodsStock.setLockNum(itemCount.get());
                            stock.setNumInWarehouseLock(stock.getNumInWarehouseLock() - itemCount.get());
                            itemCount.set(0);
                        } else {
                            orderGoodsStock.setLockNum(stock.getNumInWarehouseLock());
                            itemCount.set(itemCount.get() - stock.getNumInWarehouseLock());
                        }
                        orderGoodsStock.setGoodsId(orderGoodsDO.getGoodsId());
                        orderGoodsStock.setItemId(orderGoodsDO.getItemId());
                        orderGoodsStock.setStockId(stock.getStockId());
                        orderGoodsStock.setWarehouseId(stock.getWarehouseId());
                        orderGoodsDO.setWarehouseId(stock.getWarehouseId());
                        orderGoodsStock.setCreateTime(time);
                        orderGoodsStock.setUpdateTime(time);
                        stockDOS.add(orderGoodsStock);
                    }
                });
                if (itemCount.get() > 0 && vmiLock != null && vmiLock.getLockNum().intValue() > 0) {
                    OrderGoodsStockDO orderGoodsStock = new OrderGoodsStockDO();
                    orderGoodsStock.setLockType(StockLockType.LOCK_VMI.getValue());
                    orderGoodsStock.setLockNum(vmiLock.getLockNum().intValue());
                    orderGoodsStock.setGoodsId(orderGoodsDO.getGoodsId());
                    orderGoodsStock.setItemId(orderGoodsDO.getItemId());
                    orderGoodsStock.setStockId(vmiLock.getStockId());
                    orderGoodsStock.setWarehouseId(vmiLock.getWarehouseId());
                    orderGoodsStock.setCreateTime(time);
                    orderGoodsStock.setUpdateTime(time);
                    stockDOS.add(orderGoodsStock);
                }
                orderGoodsDO.setOrderGoodsStocks(stockDOS);
            }
            // 变更日志
            List<Object> changes = new ArrayList<>();
            changes.add(addOrderGoods);
            changeMap.put("增加行项目", changes);
        });
        return orderGoodsDOS;
    }

    /**
     * 修改 订单商品行项目
     * 
     * @param changeOrderGoodss
     * @param orderGoodsDOMap
     * @param unLockDTOS
     * @param goodsItemCountDTOMap
     * @param orderGoodsStockDOSMap
     * @param changeStockDOS
     * @param deleteOrderStockIds
     * @param changeMap
     * @param saleDTOS
     * @return
     */
    public static List<OrderGoodsDO> toChangeOrderGoodsDTOList(List<ErpOrderChangeDetailCmd> changeOrderGoodss,
        Map<Integer, OrderGoodsDO> orderGoodsDOMap, List<ItemStockLockDTO> unLockDTOS,
        Map<OrderGoodsDO, GoodsItemCountDTO> goodsItemCountDTOMap,
        Map<Integer, List<OrderGoodsStockDO>> orderGoodsStockDOSMap, List<OrderGoodsStockDO> changeStockDOS,
        List<Integer> deleteOrderStockIds, Map<String, List<Object>> changeMap, List<GoodsSaleDTO> saleDTOS) {
        List<OrderGoodsDO> changeOrderGoodsDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(changeOrderGoodss)) {
            changeOrderGoodss.forEach(changeOrderGoods -> {
                OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(changeOrderGoods.getItemOrderId());
                if (!changeOrderGoods.getNum().equals(orderGoodsDO.getItemCount())) {
                    List<Object> changes = new ArrayList<>();
                    changes.add(orderGoodsDO.getItemCount());
                    changes.add(changeOrderGoods.getNum());
                    changeMap.put(orderGoodsDO.getItemCode() + "数量变更", changes);
                    changeOrderGoodsDOS.add(orderGoodsDO);
                    log.info("订单变更：变更前：{}，变更后：{}", orderGoodsDO.getItemCount(), changeOrderGoods.getNum());
                    // 变更后-变更前 正数是多，负数是减（订单明细和锁库明细需分开计算，因为由于订单变更有可能导致锁库明细和订单明细存在不一致情况）
                    // 明细变更数量
                    AtomicInteger changeNum =
                        new AtomicInteger(changeOrderGoods.getNum() - orderGoodsDO.getItemCount());
                    // 锁库变更数量
                    AtomicInteger changeLockNum =
                        new AtomicInteger(changeOrderGoods.getNum() - orderGoodsDO.getItemCount());
                    orderGoodsDO.setItemCount(changeOrderGoods.getNum());
                    if (orderGoodsDO.getDeliverCount() != null) {
                        orderGoodsDO.setUnDeliverCount(orderGoodsDO.getItemCount() - orderGoodsDO.getDeliverCount());
                    } else {
                        orderGoodsDO.setUnDeliverCount(orderGoodsDO.getItemCount());
                    }
                    // 商品订单销售量变更
                    GoodsSaleDTO saleDTO = new GoodsSaleDTO();
                    saleDTO.setOrderGoodsId(orderGoodsDO.getId());
                    saleDTO.setGoodsId(orderGoodsDO.getGoodsId());
                    saleDTO.setItemId(orderGoodsDO.getItemId());
                    saleDTO.setSaleNum(changeNum.get());
                    saleDTOS.add(saleDTO);
                    // 历史锁库信息 同仓库 同类型锁库记录
                    List<OrderGoodsStockDO> stockDOS = orderGoodsStockDOSMap.get(orderGoodsDO.getId());
                    // 数量增加情况，只增加在库数量，减少时需判断在库、vmi和在途(数量增加情况加锁明细不在此环节，在调用库存计算后加锁明细)
                    if (changeNum.get() > 0) {
                        if (orderGoodsDO.getItemInCount() == null) {
                            orderGoodsDO.setItemInCount(changeNum.get());
                        } else {
                            orderGoodsDO.setItemInCount(orderGoodsDO.getItemInCount() + changeNum.get());
                        }
                        // 库存锁库
                        GoodsItemCountDTO goodsItemCountDTO = new GoodsItemCountDTO();
                        goodsItemCountDTO.setSupportOversoldFlag(true);
                        goodsItemCountDTO.setItemId(orderGoodsDO.getItemId());
                        goodsItemCountDTO.setOnWayCount(0);
                        goodsItemCountDTO.setInStockCount(changeNum.get());
                        goodsItemCountDTOMap.put(orderGoodsDO, goodsItemCountDTO);
                    } else {
                        // 在库数量变更(减少情况),修改订单明细反锁库存
                        // 有在库 先扣在库
                        if (changeNum.get() < 0 && orderGoodsDO.getItemInCount() != null
                            && orderGoodsDO.getItemInCount() > 0) {
                            // 直接扣完或者在库不够 还要其他库存补
                            if (orderGoodsDO.getItemInCount() + changeNum.get() <= 0) {
                                orderGoodsDO.setInChangeNum(-orderGoodsDO.getItemInCount());
                                changeNum.set(orderGoodsDO.getItemInCount() + changeNum.get());
                                orderGoodsDO.setItemInCount(0);
                            } else {
                                orderGoodsDO.setInChangeNum(changeNum.get());
                                orderGoodsDO.setItemInCount(orderGoodsDO.getItemInCount() + changeNum.get());
                                changeNum.set(0);
                            }
                        }
                        // vmi数量变更
                        if (changeNum.get() < 0 && orderGoodsDO.getItemVmiCount() != null
                            && orderGoodsDO.getItemVmiCount() > 0) {
                            if (orderGoodsDO.getItemVmiCount() + changeNum.get() <= 0) {
                                orderGoodsDO.setVmiChangeNum(-orderGoodsDO.getItemVmiCount());
                                changeNum.set(orderGoodsDO.getItemVmiCount() + changeNum.get());
                                orderGoodsDO.setItemVmiCount(0);
                            } else {
                                orderGoodsDO.setVmiChangeNum(changeNum.get());
                                orderGoodsDO.setItemVmiCount(orderGoodsDO.getItemVmiCount() + changeNum.get());
                                changeNum.set(0);
                            }
                        }
                        // 在途数量变更
                        if (changeNum.get() < 0 && orderGoodsDO.getItemOnWayCount() != null
                            && orderGoodsDO.getItemOnWayCount() > 0) {
                            if (orderGoodsDO.getItemOnWayCount() + changeNum.get() <= 0) {
                                orderGoodsDO.setOnWayChangeNum(-orderGoodsDO.getItemOnWayCount());
                                changeNum.set(orderGoodsDO.getItemOnWayCount() + changeNum.get());
                                orderGoodsDO.setItemOnWayCount(0);
                            } else {
                                orderGoodsDO.setOnWayChangeNum(changeNum.get());
                                orderGoodsDO.setItemOnWayCount(orderGoodsDO.getItemOnWayCount() + changeNum.get());
                                changeNum.set(0);
                            }
                        }
                        // 在库数量变更(减少情况),修改订单锁库明细
                        ItemStockLockDTO lockDTO = new ItemStockLockDTO();
                        lockDTO.setItemId(orderGoodsDO.getItemId());
                        unLockDTOS.add(lockDTO);
                        List<WarehouseInStockLockDTO> inStockLockDTOList = new ArrayList<>();
                        List<WarehouseInStockLockDTO> onWayLockDTOList = new ArrayList<>();
                        lockDTO.setInStockLockDTOList(inStockLockDTOList);
                        lockDTO.setOnWayLockDTOList(onWayLockDTOList);
                        // 在库锁库记录
                        List<OrderGoodsStockDO> inStockDOS = null;
                        // vmi锁库记录
                        OrderGoodsStockDO vmiStockDO = null;
                        // 在途锁库记录
                        List<OrderGoodsStockDO> onWayStockDOS = null;
                        // 计算在库反锁库
                        if (changeLockNum.get() < 0 && !CollectionUtils.isEmpty(stockDOS)) {
                            inStockDOS = stockDOS.stream()
                                .filter(stockDO -> stockDO.getWarehouseId().equals(orderGoodsDO.getWarehouseId())
                                    && stockDO.getLockType().equals(StockLockType.LOCK_IN.getValue()))
                                .collect(Collectors.toList());
                            // 计算反锁库存
                            if (!CollectionUtils.isEmpty(inStockDOS)) {
                                inStockDOS.forEach(inStockDO -> {
                                    if (changeLockNum.get() == 0) {// 扣减完成
                                        return;
                                    }
                                    WarehouseInStockLockDTO stockLockDTO = new WarehouseInStockLockDTO();
                                    stockLockDTO.setStockId(inStockDO.getStockId());
                                    stockLockDTO.setWarehouseId(inStockDO.getWarehouseId());
                                    inStockLockDTOList.add(stockLockDTO);
                                    Integer lockNum = inStockDO.getLockNum() + changeLockNum.get();
                                    if (lockNum <= 0) {
                                        stockLockDTO.setNumInWarehouseLock(inStockDO.getLockNum());
                                        // 删除在库锁库记录
                                        deleteOrderStockIds.add(inStockDO.getId());
                                        changeLockNum.set(lockNum);
                                    } else {
                                        stockLockDTO.setNumInWarehouseLock(-changeLockNum.get());
                                        // 修改在库锁库记录
                                        inStockDO.setLockNum(lockNum);
                                        inStockDO.setUpdateTime(new Date());
                                        changeStockDOS.add(inStockDO);
                                        changeLockNum.set(0);
                                    }
                                });
                            }
                        }
                        // 计算VMI反锁库
                        if (changeLockNum.get() < 0 && !CollectionUtils.isEmpty(stockDOS)) {
                            vmiStockDO = stockDOS.stream()
                                .filter(stockDO -> stockDO.getWarehouseId().equals(orderGoodsDO.getWarehouseId())
                                    && stockDO.getLockType().equals(StockLockType.LOCK_VMI.getValue()))
                                .findFirst().orElse(null);
                            // 计算反锁库存
                            if (vmiStockDO != null) {
                                VminStockLockDTO vmiLock = new VminStockLockDTO();
                                vmiLock.setStockId(vmiStockDO.getStockId());
                                vmiLock.setWarehouseId(vmiStockDO.getWarehouseId());
                                lockDTO.setVmiLock(vmiLock);
                                Integer lockNum = vmiStockDO.getLockNum() + changeLockNum.get();
                                if (lockNum <= 0) {
                                    vmiLock.setLockNum(vmiStockDO.getLockNum());
                                    // 删除在库锁库记录
                                    deleteOrderStockIds.add(vmiStockDO.getId());
                                    changeLockNum.set(lockNum);
                                } else {
                                    vmiLock.setLockNum(-changeLockNum.get());
                                    // 修改在库锁库记录
                                    vmiStockDO.setLockNum(lockNum);
                                    vmiStockDO.setUpdateTime(new Date());
                                    changeStockDOS.add(vmiStockDO);
                                    changeLockNum.set(0);
                                }
                            }
                        }
                        // 计算在途反锁库
                        if (changeLockNum.get() < 0 && !CollectionUtils.isEmpty(stockDOS)) {
                            onWayStockDOS = stockDOS.stream()
                                .filter(stockDO -> stockDO.getWarehouseId().equals(orderGoodsDO.getWarehouseId())
                                    && stockDO.getLockType().equals(StockLockType.LOCK_ON_WAY.getValue()))
                                .collect(Collectors.toList());
                            // 计算反锁库存
                            if (!CollectionUtils.isEmpty(onWayStockDOS)) {
                                onWayStockDOS.forEach(onWayStockDO -> {
                                    if (changeLockNum.get() == 0) {// 扣减完成
                                        return;
                                    }
                                    WarehouseInStockLockDTO stockLockDTO = new WarehouseInStockLockDTO();
                                    stockLockDTO.setStockId(onWayStockDO.getStockId());
                                    stockLockDTO.setWarehouseId(onWayStockDO.getWarehouseId());
                                    onWayLockDTOList.add(stockLockDTO);
                                    Integer lockNum = onWayStockDO.getLockNum() + changeLockNum.get();
                                    if (lockNum <= 0) {
                                        stockLockDTO.setNumOnWayLock(onWayStockDO.getLockNum());
                                        // 删除在库锁库记录
                                        deleteOrderStockIds.add(onWayStockDO.getId());
                                        changeLockNum.set(lockNum);
                                    } else {
                                        stockLockDTO.setNumOnWayLock(-changeLockNum.get());
                                        // 修改在库锁库记录
                                        onWayStockDO.setLockNum(lockNum);
                                        onWayStockDO.setUpdateTime(new Date());
                                        changeStockDOS.add(onWayStockDO);
                                        changeLockNum.set(0);
                                    }
                                });
                            }
                        }
                        // 在途不分仓库情况
                        // if (changeNum < 0 && orderGoodsDO.getItemOnWayCount() != null
                        // && orderGoodsDO.getItemOnWayCount() > 0) {
                        // OnWayStockLockDTO onWayLock = new OnWayStockLockDTO();
                        // OrderGoodsStockDO onWayStockDO = null;
                        // if (!CollectionUtils.isEmpty(stockDOS)) {
                        // onWayStockDO = stockDOS.stream()
                        // .filter(stockDO -> stockDO.getWarehouseId().equals(orderGoodsDO.getWarehouseId())
                        // && stockDO.getLockType().equals(StockLockType.LOCK_ON_WAY.getValue()))
                        // .findFirst().orElse(null);
                        // }
                        // if (orderGoodsDO.getItemOnWayCount() + changeNum <= 0) {
                        // orderGoodsDO.setOnWayChangeNum(-orderGoodsDO.getItemOnWayCount());
                        // orderGoodsDO.setItemOnWayCount(0);
                        // changeNum = orderGoodsDO.getItemOnWayCount() + changeNum;
                        // // 计算反锁库存
                        // if (onWayStockDO != null) {
                        // deleteOrderStockIds.add(onWayStockDO.getId());
                        // onWayLock.setLockNum(onWayStockDO.getLockNum());
                        // onWayLock.setStockId(onWayStockDO.getStockId());
                        // onWayLock.setWarehouseId(onWayStockDO.getWarehouseId());
                        // lockDTO.setOnWayLock(onWayLock);
                        // }
                        // } else {
                        // orderGoodsDO.setOnWayChangeNum(changeNum);
                        // orderGoodsDO.setItemOnWayCount(orderGoodsDO.getItemOnWayCount() + changeNum);
                        // changeNum = 0;
                        // // 计算反锁库存
                        // if (onWayStockDO != null) {
                        // onWayLock.setLockNum(orderGoodsDO.getItemInCount());
                        // onWayLock.setStockId(onWayStockDO.getStockId());
                        // onWayLock.setWarehouseId(onWayStockDO.getWarehouseId());
                        // onWayStockDO.setLockNum(orderGoodsDO.getItemInCount());
                        // lockDTO.setOnWayLock(onWayLock);
                        // changeStockDOS.add(onWayStockDO);
                        // }
                        // }
                        // }
                    }
                }
            });
        }
        return changeOrderGoodsDOS;
    }

    public static List<OrderGoodsDO> toDeleteOrderGoodsDTOList(List<ErpOrderChangeDetailCmd> deleteOrderGoodss,
        Map<Integer, OrderGoodsDO> orderGoodsDOMap, Map<Integer, List<OrderGoodsStockDO>> orderGoodsStockDOSMap,
        List<ItemStockLockDTO> unLockDTOS, Map<String, List<Object>> changeMap, List<GoodsSaleDTO> saleDTOS) {
        List<OrderGoodsDO> deleteOrderGoodsDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(deleteOrderGoodss)) {
            deleteOrderGoodss.forEach(deleteOrderGoods -> {
                OrderGoodsDO orderGoodsDO = orderGoodsDOMap.get(deleteOrderGoods.getItemOrderId());
                // 商品订单销售量变更
                GoodsSaleDTO saleDTO = new GoodsSaleDTO();
                saleDTO.setOrderGoodsId(orderGoodsDO.getId());
                saleDTO.setGoodsId(orderGoodsDO.getGoodsId());
                saleDTO.setItemId(orderGoodsDO.getItemId());
                saleDTO.setSaleNum(-orderGoodsDO.getUnDeliverCount());
                saleDTOS.add(saleDTO);
                List<Object> changes = new ArrayList<>();
                changes.add(orderGoodsDO.getItemCode());
                changeMap.put("删除行项目", changes);
                deleteOrderGoodsDOS.add(orderGoodsDO);
                // 计算库存反锁
                List<OrderGoodsStockDO> orderGoodsStockDOS = orderGoodsStockDOSMap.get(orderGoodsDO.getId());
                if (!CollectionUtils.isEmpty(orderGoodsStockDOS)) {
                    ItemStockLockDTO lockDTO = new ItemStockLockDTO();
                    unLockDTOS.add(lockDTO);
                    lockDTO.setItemId(orderGoodsDO.getItemId());
                    List<WarehouseInStockLockDTO> stockLockDTOS = new ArrayList<>();
                    List<WarehouseInStockLockDTO> onWayLockDTOS = new ArrayList<>();
                    lockDTO.setInStockLockDTOList(stockLockDTOS);
                    lockDTO.setOnWayLockDTOList(onWayLockDTOS);
                    orderGoodsStockDOS.forEach(orderGoodsStockDO -> {
                        if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_1)) {
                            WarehouseInStockLockDTO stockLockDTO = new WarehouseInStockLockDTO();
                            stockLockDTO.setWarehouseId(orderGoodsStockDO.getWarehouseId());
                            stockLockDTO.setStockId(orderGoodsStockDO.getStockId());
                            stockLockDTO.setNumInWarehouseLock(orderGoodsStockDO.getLockNum());
                            stockLockDTOS.add(stockLockDTO);
                        } else if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_2)) {
                            WarehouseInStockLockDTO stockLockDTO = new WarehouseInStockLockDTO();
                            stockLockDTO.setWarehouseId(orderGoodsStockDO.getWarehouseId());
                            stockLockDTO.setStockId(orderGoodsStockDO.getStockId());
                            stockLockDTO.setNumInWarehouseLock(orderGoodsStockDO.getLockNum());
                            onWayLockDTOS.add(stockLockDTO);
                            // OnWayStockLockDTO onWayLock = new OnWayStockLockDTO();
                            // onWayLock.setWarehouseId(orderGoodsStockDO.getWarehouseId());
                            // onWayLock.setStockId(orderGoodsStockDO.getStockId());
                            // onWayLock.setLockNum(orderGoodsStockDO.getLockNum());
                            // lockDTO.setOnWayLock(onWayLock);
                        } else if (orderGoodsStockDO.getLockType().equals(Constant.LOCK_TYPE_3)) {
                            VminStockLockDTO vmiLock = new VminStockLockDTO();
                            vmiLock.setWarehouseId(orderGoodsStockDO.getWarehouseId());
                            vmiLock.setStockId(orderGoodsStockDO.getStockId());
                            vmiLock.setLockNum(orderGoodsStockDO.getLockNum());
                            lockDTO.setVmiLock(vmiLock);
                        }
                    });
                }
            });
        }
        return deleteOrderGoodsDOS;
    }

    /**
     * 设置B2B的行序号为ERP的内码明细Id、判断之前是否已设置过行序号
     * 
     * @param orderGoodsDOList
     * @param entryIds
     */
    public static Boolean updateSerialNumberToErpEntryId(List<OrderGoodsDO> orderGoodsDOList,
        List<ErpOrderDetailEntryId> entryIds) {
        if (entryIds == null || entryIds.size() == 0) {
            // 没有返回行序号
            return false;
        }
        Map<Integer, Integer> map = new HashMap<>();
        entryIds.stream().forEach(erpOrderDetailEntryId -> {
            map.put(erpOrderDetailEntryId.getFSerialNumber(), erpOrderDetailEntryId.getErpEntryId());
        });
        // 判断是否已设置了行序号
        for (int x = 0; x < orderGoodsDOList.size(); x++) {
            if (orderGoodsDOList.get(x).getSerialNumber() - entryIds.get(0).getErpEntryId() == 0) {
                // 之前已设置了行序号、不再修改、直接返回
                return true;
            }
        }

        orderGoodsDOList.stream().forEach(orderGoodsDO -> {
            Integer erpEntryId = map.get(orderGoodsDO.getSerialNumber());
            if (erpEntryId == null) {
                throw OrderException.buildException(OrderGoodsErrorCode.O_ORDER_GOODS_SERIAL_NUMBER_DISAGREE_ERP_CODE);
            }
            orderGoodsDO.setSerialNumber(erpEntryId);
            orderGoodsDO.setUpdateTime(new Date());
        });
        // 第一次设置行序号
        return false;
    }

    public static List<GoodsItemCountDTO> toGoodsItemCountDTO(Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap,
        List<ErpOrderChangeDetailCmd> addOrderGoodss) {
        List<GoodsItemCountDTO> goodsItemCountDTOS = new ArrayList<>();
        addOrderGoodss.forEach(addOrderGoods -> {
            GoodsItemRpcDTO goodsItemRpcDTO = goodsItemRpcDTOMap.get(addOrderGoods.getItemNo());
            GoodsItemCountDTO goodsItemCountDTO = new GoodsItemCountDTO();
            goodsItemCountDTO.setItemId(goodsItemRpcDTO.getId());
            goodsItemCountDTO.setInStockCount(addOrderGoods.getNum());
            goodsItemCountDTO.setOnWayCount(0);
            goodsItemCountDTO.setSupportOversoldFlag(true);
            goodsItemCountDTOS.add(goodsItemCountDTO);
        });
        return goodsItemCountDTOS;
    }
}
