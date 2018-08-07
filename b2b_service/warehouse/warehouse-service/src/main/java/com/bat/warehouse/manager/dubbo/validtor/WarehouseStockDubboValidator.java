package com.bat.warehouse.manager.dubbo.validtor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bat.warehouse.manager.common.constant.WarehouseStockChangeLogConstant;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseDubboApiErrorCode;
import com.bat.warehouse.manager.common.error.WarehouseNameErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.stock.dto.OrderGoodsStockSimpleDTO;
import com.bat.dubboapi.order.stock.dto.OrderGoodsStockUnLockDTO;
import com.bat.dubboapi.warehouse.stock.dto.*;
import com.bat.warehouse.api.base.common.exception.WarehouseDubboApiException;
import com.bat.warehouse.api.base.util.MessageUtils;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import com.bat.warehouse.mq.dto.OrderErpNoLineDTO;

public class WarehouseStockDubboValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseStockDubboValidator.class);

    public static List<Long> checkItemErp(GoodsMsgRpcDTO goodsMsgRpcDTO) {
        List<Long> itemErpIdList = new ArrayList<>();
        if (goodsMsgRpcDTO.getGoodsId() == null) {
            throw WarehouseDubboApiException.buildException(WarehouseCommonErrorCode.COMMON_GOODS_ID_NULL);
        }
        List<GoodsItemErpDTO> itemErpDTOList = goodsMsgRpcDTO.getItemErpDTOList();
        if (itemErpDTOList == null || itemErpDTOList.size() == 0) {
            throw WarehouseDubboApiException.buildException(WarehouseCommonErrorCode.COMMON_ITEM_LIST_NULL);
        }
        for (int x = 0; x < itemErpDTOList.size(); x++) {
            GoodsItemErpDTO goodsItemErpDTO = itemErpDTOList.get(x);
            if (goodsItemErpDTO.getItemErpId() == null) {
                throw WarehouseDubboApiException.buildException(WarehouseCommonErrorCode.COMMON_ITEM_ERP_ID_NULL);
            }
            if (goodsItemErpDTO.getItemId() == null) {
                throw WarehouseDubboApiException.buildException(WarehouseCommonErrorCode.ITEM_ID_NULL);
            }
            itemErpIdList.add(goodsItemErpDTO.getItemErpId().longValue());
        }
        return itemErpIdList;
    }

    /**
     * 校验锁库参数
     * 
     * @param itemCountList
     *            下单的货品列表
     * @param areaIdList
     *            分销商所属销售区域id
     * @param distributorId
     *            分销商id
     * @param notSupportOnWayList
     *            不支持在途的货品id列表
     */
    public static void checkLockParam(List<GoodsItemCountDTO> itemCountList, List<Integer> areaIdList,
        Integer distributorId, List<Integer> notSupportOnWayList) {
        // 校验下单货品参数
        if (CollectionUtils.isEmpty(itemCountList)) {
            throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_ITEM_LOCK_STOCK_LIST_NULL);
        }
        // 分销商和销售区域列表不能同时为空
        if (CollectionUtils.isEmpty(areaIdList) && distributorId == null) {
            throw WarehouseDubboApiException
                .buildException(WarehouseDubboApiErrorCode.W_DISTRIBUTOR_ID_AND_AREA_ID_LIST_ALL_NULL);
        }
        itemCountList.stream().forEach(goodsItemCountDTO -> {
            if (goodsItemCountDTO.getItemId() == null) {
                throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_ITEM_ID_NULL);
            }
            if (goodsItemCountDTO.getInStockCount() == null) {
                throw WarehouseDubboApiException
                    .buildException(WarehouseDubboApiErrorCode.W_ITEM_IN_STOCK_LOCK_COUNT_NULL);
            }
            if (goodsItemCountDTO.getOnWayCount() == null) {
                throw WarehouseDubboApiException
                    .buildException(WarehouseDubboApiErrorCode.W_ITEM_ON_WAY_LOCK_COUNT_NULL);
            }
            if (goodsItemCountDTO.getInStockCount() < 0 || goodsItemCountDTO.getOnWayCount() < 0) {
                throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_COMMON_QUANTITY_ILLEGAL);
            }
            if (goodsItemCountDTO.getInStockCount() == 0 && goodsItemCountDTO.getOnWayCount() == 0) {
                throw WarehouseDubboApiException.buildException(
                    WarehouseDubboApiErrorCode.W_ITEM_IN_STOCK_LOCK_COUNT_AND_ON_WAY_LOCK_COUNT_ALL_ZERO);
            }
            if (goodsItemCountDTO.getOnWayCount() > 0 && !CollectionUtils.isEmpty(notSupportOnWayList)
                && notSupportOnWayList.contains(goodsItemCountDTO.getItemId())) {
                // 不支持在途、且又下了在途货品
                throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_ITEM_NOT_SUPPORT_ON_WAY);
            }
        });
    }

    /**
     * 校验订单锁库解锁参数
     * 
     * @param itemStockLockDTOList
     */
    public static void checkUnLockParam(List<ItemStockLockDTO> itemStockLockDTOList) {
        if (CollectionUtils.isEmpty(itemStockLockDTOList)) {
            throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_ITEM_UN_LOCK_LIST_NULL);
        }
        itemStockLockDTOList.forEach(itemStockLockDTO -> {
            if (itemStockLockDTO.getItemId() == null) {
                throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_ITEM_ID_NULL);
            }
            List<WarehouseInStockLockDTO> inStockLockDTOList = itemStockLockDTO.getInStockLockDTOList();
            VminStockLockDTO vmiLock = itemStockLockDTO.getVmiLock();
            // 校验在库(和在途)
            validUnLockInStock(inStockLockDTOList);
            // 校验VMI
            validUnLockVmi(vmiLock);
        });
    }

    private static void validUnLockVmi(VminStockLockDTO vmiLock) {
        if (vmiLock == null) {
            return;
        }
        if (vmiLock.getLockNum() == null || vmiLock.getLockNum() < 1) {
            throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_COMMON_QUANTITY_ILLEGAL);
        }
    }

    /**
     * 校验解锁在途
     * 
     * @param onWayLock
     */
    private static void validUnLockOnWay(OnWayStockLockDTO onWayLock) {
        if (onWayLock == null) {
            return;
        }

        if (onWayLock.getLockNum() == null) {
            throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_COMMON_QUANTITY_ILLEGAL);
        }
        if (onWayLock.getLockNum() < 1) {
            throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_COMMON_QUANTITY_ILLEGAL);
        }
    }

    /**
     * 检查在途在库数据
     * 
     * @param inStockLockDTOList
     */
    private static void validUnLockInStock(List<WarehouseInStockLockDTO> inStockLockDTOList) {
        if (CollectionUtils.isEmpty(inStockLockDTOList)) {
            return;
        }
        inStockLockDTOList.forEach(warehouseInStockLockDTO -> {
            if (warehouseInStockLockDTO.getWarehouseId() == null) {
                throw WarehouseDubboApiException
                    .buildException(MessageUtils.get(WarehouseNameErrorCode.ERROR_NAME_WAREHOUSE)
                        + MessageUtils.get(WarehouseCommonErrorCode.ID_NULL));
            }
            if (warehouseInStockLockDTO.getNumInWarehouseLock() == null
                && warehouseInStockLockDTO.getNumOnWayLock() == null) {
                throw WarehouseDubboApiException.buildException(
                    WarehouseDubboApiErrorCode.W_ITEM_IN_STOCK_LOCK_COUNT_AND_ON_WAY_LOCK_COUNT_ALL_ZERO);
            }
            if (warehouseInStockLockDTO.getNumInWarehouseLock() != null
                && warehouseInStockLockDTO.getNumInWarehouseLock() < 0) {
                throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_COMMON_QUANTITY_ILLEGAL);
            }
            if (warehouseInStockLockDTO.getNumOnWayLock() != null && warehouseInStockLockDTO.getNumOnWayLock() < 0) {
                throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_COMMON_QUANTITY_ILLEGAL);
            }
        });
    }

    /**
     * 库存变更、移除ERP传过来仓库编码非B2B所拥有的仓库
     * 
     * @param stockBillDetailList
     */
    public static void removeNotBelongB2BWarehouse(List<ErpItemStockChangeCmd> stockBillDetailList,
        List<WarehouseDO> warehouseDOList, String vmiWarehouseNo) {
        // 为了跟之前逻辑一致 把VMI 踢出去
        warehouseDOList = warehouseDOList.stream()
            .filter(warehouseDO -> !warehouseDO.getWarehouseNo().equals(vmiWarehouseNo)).collect(Collectors.toList());
        Map<String, WarehouseDO> warehouseDOMap =
            warehouseDOList.stream().collect(Collectors.toMap(WarehouseDO::getWarehouseNo, warehouseDO -> warehouseDO));
        for (int x = 0; x < stockBillDetailList.size(); x++) {
            ErpItemStockChangeCmd erpItemStockChangeCmd = stockBillDetailList.get(x);
            // VMI
            if (vmiWarehouseNo.equals(erpItemStockChangeCmd.getWarehouseNo())) {
                continue;
            }
            WarehouseDO warehouseDO = warehouseDOMap.get(erpItemStockChangeCmd.getWarehouseNo());
            if (warehouseDO == null) {
                // 仓库编码非B2B的、忽略
                stockBillDetailList.remove(x);
                x--;
            } else {
                // 设置仓库id、便于扣减库存
                erpItemStockChangeCmd.setWarehouseId(warehouseDO.getId());
            }
        }
    }

    /**
     * 参数校验、返回所有的ERP单号和明细行序号列表
     * 
     * @param
     * @return
     */
    public static List<OrderErpNoLineDTO>
        validWarehouseStockChangeFromErp(List<ErpItemStockChangeCmd> stockBillDetails) {
        List<OrderErpNoLineDTO> orderErpNoLineDTOS = new ArrayList<>();
        Map<String, Map<Integer, Integer>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(stockBillDetails)) {
            throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_ERP_STOCK_CHANGE_LIST_NULL);
        }
        for (ErpItemStockChangeCmd erpItemStockChangeCmd : stockBillDetails) {
            if (StringUtils.isBlank(erpItemStockChangeCmd.getItemNo())) {
                // 判断80码
                throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_ITEM_CODE_NULL);
            }
            if (StringUtils.isBlank(erpItemStockChangeCmd.getOrderErpNo())) {
                continue;
            }
            Map<Integer, Integer> lineNumMap = map.get(erpItemStockChangeCmd.getOrderErpNo());
            if (CollectionUtils.isEmpty(lineNumMap)) {
                lineNumMap = new HashMap<>();
                map.put(erpItemStockChangeCmd.getOrderErpNo(), lineNumMap);
            }
            lineNumMap.put(erpItemStockChangeCmd.getLineNumberId(), erpItemStockChangeCmd.getNum());
            // 手动设置为减少
            if (erpItemStockChangeCmd.getNum() < 0) {
                erpItemStockChangeCmd.setChangeType(WarehouseStockChangeLogConstant.CHANGE_TYPE_REDUCE);
            } else {
                erpItemStockChangeCmd.setChangeType(WarehouseStockChangeLogConstant.CHANGE_TYPE_ADD);
            }
        }
        for (Map.Entry<String, Map<Integer, Integer>> entry : map.entrySet()) {
            OrderErpNoLineDTO orderErpNoLineDTO = new OrderErpNoLineDTO();
            orderErpNoLineDTO.setOrderErpNo(entry.getKey());
            orderErpNoLineDTO.setLineNumberMap(entry.getValue());
            orderErpNoLineDTOS.add(orderErpNoLineDTO);
        }
        return orderErpNoLineDTOS;
    }

    /**
     * //移除B2B没有的货品
     * 
     * @param stockBillDetailList
     * @param warehouseInStockDOList
     */
    public static void removeNotBelongB2BItem(List<ErpItemStockChangeCmd> stockBillDetailList,
        List<WarehouseInStockDO> warehouseInStockDOList) {
        Map<String, List<WarehouseInStockDO>> warehouseInStockDOMap =
            warehouseInStockDOList.stream().collect(Collectors.groupingBy(WarehouseInStockDO::getItemCode));
        for (int x = 0; x < stockBillDetailList.size(); x++) {
            ErpItemStockChangeCmd erpItemStockChangeCmd = stockBillDetailList.get(x);
            List<WarehouseInStockDO> inStockDOS = warehouseInStockDOMap.get(erpItemStockChangeCmd.getItemNo());
            if (CollectionUtils.isEmpty(inStockDOS)) {
                // 没有该货品、删除
                stockBillDetailList.remove(x);
                x--;
            } else {
                Map<Integer, WarehouseInStockDO> inStockDOMap = inStockDOS.stream().collect(
                    Collectors.toMap(WarehouseInStockDO::getWarehouseId, warehouseInStockDO -> warehouseInStockDO));
                WarehouseInStockDO warehouseInStockDO = inStockDOMap.get(erpItemStockChangeCmd.getWarehouseId());
                // 设置货品id、下一步根据货品id进行锁库
                erpItemStockChangeCmd.setItemId(warehouseInStockDO.getItemId());
            }
        }
    }

    /**
     * 判断ERP数量是否超出锁库数量、 order_goods_stock解锁的列表
     *
     * @param orderUnLockStockMap
     * @param stockBillDetailList
     * @param allwarehouseInStockDOList
     * @return
     */
    public static List<OrderGoodsStockUnLockDTO> validChangeNum(
        Map<String, List<OrderGoodsStockSimpleDTO>> orderUnLockStockMap,
        List<ErpItemStockChangeCmd> stockBillDetailList, List<WarehouseInStockDO> allwarehouseInStockDOList) {
        if (orderUnLockStockMap == null || orderUnLockStockMap.size() == 0) {
            return null;
        }
        List<OrderGoodsStockUnLockDTO> unLockDTOList = new ArrayList<>();
        Map<Integer, WarehouseInStockDO> itemMap = allwarehouseInStockDOList.stream()
            .collect(Collectors.toMap(WarehouseInStockDO::getItemId, warehouseInStockDO -> warehouseInStockDO));
        stockBillDetailList.forEach(erpItemStockChangeCmd -> {
            List<OrderGoodsStockSimpleDTO> orderGoodsStockSimpleDTOList = orderUnLockStockMap
                .get(erpItemStockChangeCmd.getOrderErpNo() + "_" + erpItemStockChangeCmd.getLineNumberId());
            if (orderGoodsStockSimpleDTOList != null && orderGoodsStockSimpleDTOList.size() > 0) {
                // 属于B2B的订单明细
                // 判断增加库存数量（下单10个、实际只出了1个、相当于增加了9个）是否超出下单锁库数量
                Integer num = erpItemStockChangeCmd.getNum();
                // 锁库数量
                Integer lockSum = 0;
                for (int x = 0; x < orderGoodsStockSimpleDTOList.size(); x++) {
                    OrderGoodsStockSimpleDTO orderGoodsStockSimpleDTO = orderGoodsStockSimpleDTOList.get(x);
                    OrderGoodsStockUnLockDTO unLockDTO = new OrderGoodsStockUnLockDTO();
                    unLockDTO.setUnLockQuantity(erpItemStockChangeCmd.getNum());
                    unLockDTO.setId(orderGoodsStockSimpleDTO.getId());
                    lockSum += orderGoodsStockSimpleDTO.getLockNum();
                    unLockDTOList.add(unLockDTO);
                }
                if (WarehouseStockChangeLogConstant.CHANGE_TYPE_REDUCE.equals(erpItemStockChangeCmd.getChangeType())) {
                    // 库存减少、传的是负数
                    num = -num;
                }
                if (num > lockSum) {
                    WarehouseInStockDO warehouseInStockDO =
                        itemMap.get(orderGoodsStockSimpleDTOList.get(0).getItemId());
                    if (WarehouseStockChangeLogConstant.CHANGE_TYPE_REDUCE
                        .equals(erpItemStockChangeCmd.getChangeType())) {
                        throw WarehouseDubboApiException
                            .buildException(warehouseInStockDO.getItemName() + "只锁了" + lockSum + ",但库存却减少了" + num);
                    }
                    throw WarehouseDubboApiException.buildException(warehouseInStockDO.getItemName() + "只锁了" + lockSum
                        + ",但库存却增加了" + erpItemStockChangeCmd.getNum());
                }
            }
        });
        return unLockDTOList;
    }

    /**
     * 校验下单锁库和解锁混合接口参数
     * 
     * @param itemCountList
     *            该列表不为空、表示含有锁库操作
     * @param areaIdList
     * @param distributorId
     * @param notSupportOnWayList
     * @param unLockItemList
     *            解锁列表
     */
    public static void checkLockAndUnLockParamHybrid(List<GoodsItemCountDTO> itemCountList, List<Integer> areaIdList,
        Integer distributorId, List<Integer> notSupportOnWayList, List<ItemStockLockDTO> unLockItemList) {
        Boolean isAllNullFlag = true;
        if (itemCountList != null && itemCountList.size() > 0) {
            // 含有锁库
            isAllNullFlag = false;
            checkLockParam(itemCountList, areaIdList, distributorId, notSupportOnWayList);
        }
        if (unLockItemList != null && unLockItemList.size() > 0) {
            // 校验解锁
            isAllNullFlag = false;
            checkUnLockParam(unLockItemList);
        }
        if (isAllNullFlag) {
            // 锁库和解锁列表不能都为空
            throw WarehouseDubboApiException
                .buildException(WarehouseDubboApiErrorCode.W_LOCK_STOCK_LIST_AND_UN_LOCK_LIST_ALL_NULL);
        }

    }
}
