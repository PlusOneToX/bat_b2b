package com.bat.warehouse.manager.dubbo.convertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.bat.warehouse.manager.common.error.WarehouseDubboApiErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.bat.dubboapi.order.stock.dto.OrderErpNoLineNumberIdList;
import com.bat.dubboapi.order.stock.dto.OrderGoodsStockSimpleDTO;
import com.bat.dubboapi.warehouse.stock.dto.ErpItemStockChangeCmd;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemCountDTO;
import com.bat.warehouse.api.base.common.exception.WarehouseDubboApiException;
import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;

public class WarehouseStockDubboConvertor {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseStockDubboConvertor.class);

    /**
     * 计算在库在库可下单量
     * 
     * @param list
     * @param goodsVmiStockDO
     * @return
     */
    public static List<Integer> calculateInStockUsable(List<WarehouseInStockDO> list, GoodsVmiStockDO goodsVmiStockDO) {
        List<Integer> stockQuantity = new ArrayList<>();
        AtomicReference<Integer> inStockUsableQuantity = new AtomicReference<>(0);
        AtomicReference<Integer> onWayUsableQuantity = new AtomicReference<>(0);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(warehouseInStockDO -> {
                // 在库可下单量=在库数量-在库锁定数量-预留数量-在途锁定数量
                Integer inStockQuantity =
                    warehouseInStockDO.getNumInWarehouse() - warehouseInStockDO.getNumInWarehouseLock()
                        - warehouseInStockDO.getNumReserved() - warehouseInStockDO.getNumOnWayLock();
                // 在途可下单量计算
                // 当inStockQuantity <0 时,在途可下单量 = 在途数量 + inStockQuantity
                // 当inStockQuantity >0 时,在途可下单量 = 在途数量
                Integer onWayQuantity = 0;
                if (inStockQuantity < 0) {
                    onWayQuantity = warehouseInStockDO.getNumOnWay() + inStockQuantity;
                } else {
                    onWayQuantity = warehouseInStockDO.getNumOnWay();
                }
                inStockUsableQuantity.set(inStockUsableQuantity.get() + inStockQuantity);
                onWayUsableQuantity.set(onWayUsableQuantity.get() + onWayQuantity);
            });
        }
        // VMI仓库当作在库处理
        if (goodsVmiStockDO != null) {
            // 在库可下单量=在库可下单量 +VMI在库 - VMI锁定
            Integer inStockQuantity =
                inStockUsableQuantity.get() + goodsVmiStockDO.getNumVmi() - goodsVmiStockDO.getNumLock();
            inStockUsableQuantity.set(inStockQuantity);
            // inStockQuantity<0,说明锁定数量大于在库数量，不够锁的数量应该在在途数量中扣减
            if (inStockQuantity < 0) {
                onWayUsableQuantity.set(onWayUsableQuantity.get() + inStockQuantity);
            }
        }
        // 在途在库可下单量小于零统一设成零
        if (inStockUsableQuantity.get() < 0) {
            inStockUsableQuantity.set(0);
        }
        if (onWayUsableQuantity.get() < 0) {
            onWayUsableQuantity.set(0);
        }
        stockQuantity.add(inStockUsableQuantity.get());
        stockQuantity.add(onWayUsableQuantity.get());
        return stockQuantity;
    }

    /**
     * 参数校验、返回所有的ERP单号和明细行序号列表
     * 
     * @param stockBillDetails
     * @return
     */
    public static List<OrderErpNoLineNumberIdList>
        toOrderErpNoLineNumberIdList(List<ErpItemStockChangeCmd> stockBillDetails) {
        List<OrderErpNoLineNumberIdList> list = new ArrayList<>();
        Map<String, List<Integer>> map = new HashMap<>();
        if (stockBillDetails == null || stockBillDetails.size() == 0) {
            throw WarehouseDubboApiException.buildException(WarehouseDubboApiErrorCode.W_ERP_STOCK_CHANGE_LIST_NULL);
        }
        for (int x = 0; x < stockBillDetails.size(); x++) {
            ErpItemStockChangeCmd erpItemStockChangeCmd = stockBillDetails.get(x);

            List<Integer> lineNumIdList = map.get(erpItemStockChangeCmd.getOrderErpNo());
            if (lineNumIdList == null || lineNumIdList.size() == 0) {
                lineNumIdList = new ArrayList<>();
            }
            lineNumIdList.add(erpItemStockChangeCmd.getLineNumberId());
            map.put(erpItemStockChangeCmd.getOrderErpNo(), lineNumIdList);
        }
        return list;
    }

    /**
     * //转换成 ItemNo为key、同一个itemNo做成list作为value
     * 
     * @param stockBillDetailList
     * @return
     */
    public static Map<String, List<ErpItemStockChangeCmd>>
        toItemNOChangeListMap(List<ErpItemStockChangeCmd> stockBillDetailList) {

        Map<String,
            List<ErpItemStockChangeCmd>> map = stockBillDetailList.stream()
                .collect(Collectors.toMap(ErpItemStockChangeCmd::getItemNo,
                    erpItemStockChangeCmd -> Lists.newArrayList(erpItemStockChangeCmd),
                    (List<ErpItemStockChangeCmd> newValueList, List<ErpItemStockChangeCmd> oldValueList) -> {
                        oldValueList.addAll(newValueList);
                        return oldValueList;
                    }));
        return map;
    }

    public static void main(String[] args) {
        List<ErpItemStockChangeCmd> stockBillDetailList = new ArrayList<>();
        ErpItemStockChangeCmd erpItemStockChangeCmd = new ErpItemStockChangeCmd();
        erpItemStockChangeCmd.setItemNo("001");
        erpItemStockChangeCmd.setChangeType((short)1);
        erpItemStockChangeCmd.setNum(1);
        stockBillDetailList.add(erpItemStockChangeCmd);
        ErpItemStockChangeCmd erpItemStockChangeCmd2 = new ErpItemStockChangeCmd();
        erpItemStockChangeCmd2.setItemNo("002");
        erpItemStockChangeCmd2.setChangeType((short)2);
        erpItemStockChangeCmd2.setNum(22);
        stockBillDetailList.add(erpItemStockChangeCmd2);
        ErpItemStockChangeCmd erpItemStockChangeCmd3 = new ErpItemStockChangeCmd();
        erpItemStockChangeCmd3.setItemNo("003");
        erpItemStockChangeCmd3.setChangeType((short)1);
        erpItemStockChangeCmd3.setNum(1);
        stockBillDetailList.add(erpItemStockChangeCmd3);
        System.out.println(JSON.toJSONString(toItemNOChangeListMap(stockBillDetailList)));
    }

    public static Map<String, List<OrderGoodsStockSimpleDTO>>
        toUnLockStockMap(List<OrderGoodsStockSimpleDTO> goodsStockSimpleDTOList) {
        Map<String, List<OrderGoodsStockSimpleDTO>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(goodsStockSimpleDTOList)) {
            // 不要返回null、避免下一步报错
            return map;
        }
        goodsStockSimpleDTOList.forEach(orderGoodsStockSimpleDTO -> {
            // 可能一个明细、锁库明细有在库+在途+VMI三种、所以行序号对应的锁库是一个列表
            String key = orderGoodsStockSimpleDTO.getOrderErpNo() + "_" + orderGoodsStockSimpleDTO.getSerialNumber();
            List<OrderGoodsStockSimpleDTO> dtoList = map.get(key);
            if (dtoList == null) {
                dtoList = new ArrayList<>();
            }
            dtoList.add(orderGoodsStockSimpleDTO);
            map.put(key, dtoList);
        });
        return map;
    }

    public static Map<Integer, List<GoodsItemCountDTO>> toItemLockStockMap(List<GoodsItemCountDTO> itemCountList,
        Map<Integer, Integer> allItemMap) {
        Map<Integer, List<GoodsItemCountDTO>> map = new HashMap<>();
        if (itemCountList == null || itemCountList.size() == 0) {
            return map;
        }
        itemCountList.stream().forEach(goodsItemCountDTO -> {
            List<GoodsItemCountDTO> goodsItemCountDTOList = map.get(goodsItemCountDTO.getItemId());
            if (goodsItemCountDTOList == null || goodsItemCountDTOList.size() == 0) {
                goodsItemCountDTOList = new ArrayList<>();
            }
            goodsItemCountDTOList.add(goodsItemCountDTO);
            map.put(goodsItemCountDTO.getItemId(), goodsItemCountDTOList);
            allItemMap.put(goodsItemCountDTO.getItemId(), 1);
        });
        return map;
    }

    /* public static Map<Integer, ItemStockLockDTO> toItemUnLockStockMap(List<ItemStockLockDTO> unLockItemList, Map<Integer, Integer> allItemMap) {
    }*/
}
