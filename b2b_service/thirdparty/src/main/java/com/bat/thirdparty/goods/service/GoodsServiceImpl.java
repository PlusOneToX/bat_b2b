package com.bat.thirdparty.goods.service;

import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.goods.GoodsItemConstant;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.StopGoodsItemRpcDTO;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bat.thirdparty.goods.api.GoodsServiceI;
import com.bat.thirdparty.goods.api.dto.ItemCodeCmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsServiceI {

    @DubboReference(check = false, timeout = 100000)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false, timeout = 100000)
    private WarehouseStockServiceRpc warehouseStockServiceRpc;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Override
    public Response syncGoodsPriceByItemCodes(ItemCodeCmd cmd) {
        return null;
    }

    @Override
    public void downSaleStatus() {
        LOGGER.info("处理下架");
        com.bat.dubboapi.goods.common.Response<List<StopGoodsItemRpcDTO>> goodsResponse =
            goodsServiceRpc.listLifeCycleStopGoodsItem(GoodsItemConstant.SALE_STATUS_UPPER);
        if (!goodsResponse.isSuccess() || goodsResponse.getData() == null) {
            return;
        }
        List<StopGoodsItemRpcDTO> stopGoodsItems = goodsResponse.getData();
        List<Integer> itemIds = stopGoodsItems.stream().map(StopGoodsItemRpcDTO::getId).collect(Collectors.toList());
        LOGGER.info("下架-查询到的货品ids:{}", itemIds);
        com.bat.dubboapi.warehouse.common.Response<WarehouseStockInfoRpcDTO> stockResponse =
            warehouseStockServiceRpc.getStockByItemIds(itemIds);
        if (stockResponse == null || stockResponse.getData() == null) {
            return;
        }
        WarehouseStockInfoRpcDTO warehouseInStockInfo = stockResponse.getData();
        LOGGER.info("下架-查询到的库存信息(与在库信息):{}", JSONObject.toJSONString(warehouseInStockInfo));

        Map<Integer, List<WarehouseInStockRpcDTO>> inStockGroup = new HashMap<>();
        // Map<Integer, GoodsOnWayStockRpcDTO> goodsOnWayStockMap;
        Map<Integer, GoodsVmiStockRpcDTO> goodsVmiStockMap;

        List<WarehouseInStockRpcDTO> warehouseInStockRpcDTOS = warehouseInStockInfo.getWarehouseInStockRpcDTOS();
        if (warehouseInStockRpcDTOS != null && warehouseInStockRpcDTOS.size() > 0) {
            inStockGroup =
                warehouseInStockRpcDTOS.stream().collect(Collectors.groupingBy(WarehouseInStockRpcDTO::getItemId));
        }

        // List<GoodsOnWayStockRpcDTO> goodsOnWayStocks = warehouseInStockInfo.getGoodsOnWayStockRpcDTOS();
        // if (goodsOnWayStocks == null) {
        // LOGGER.info("下架-在途库存为空指针，无法处理直接返回");
        // return;
        // }
        // goodsOnWayStockMap = goodsOnWayStocks.stream().collect(Collectors.toMap(GoodsOnWayStockRpcDTO::getItemId, a
        // -> a, (k1, k2) -> k1));

        List<GoodsVmiStockRpcDTO> goodsVmiStocks = warehouseInStockInfo.getGoodsVmiStockRpcDTOS();
        if (goodsVmiStocks == null) {
            LOGGER.info("下架-VMI库存为空指针，无法处理直接返回");
            return;
        }
        goodsVmiStockMap =
            goodsVmiStocks.stream().collect(Collectors.toMap(GoodsVmiStockRpcDTO::getItemId, a -> a, (k1, k2) -> k1));

        List<Integer> willDown = new ArrayList<>();
        for (Integer itemId : itemIds) {
            GoodsItemInventoryRpcDTO goodsItemInventory = new GoodsItemInventoryRpcDTO();
            List<WarehouseInStockRpcDTO> warehouseInStocks = inStockGroup.get(itemId);
            if (warehouseInStocks != null) {
                for (WarehouseInStockRpcDTO warehouseInStock : warehouseInStocks) {

                    if (warehouseInStock.getNumInWarehouse() == null) {
                        warehouseInStock.setNumInWarehouse(0);
                    }

                    goodsItemInventory.setNumInWarehouse(
                            goodsItemInventory.getNumInWarehouse() + warehouseInStock.getNumInWarehouse());

                    if (warehouseInStock.getNumReserved() == null) {
                        warehouseInStock.setNumReserved(0);
                    }

                    goodsItemInventory
                            .setNumReserved(goodsItemInventory.getNumReserved() + warehouseInStock.getNumReserved());

                    if (warehouseInStock.getNumInWarehouseLock() == null) {
                        warehouseInStock.setNumInWarehouseLock(0);
                    }

                    goodsItemInventory.setNumInWarehouseLock(
                            goodsItemInventory.getNumInWarehouseLock() + warehouseInStock.getNumInWarehouseLock());

                    if(warehouseInStock.getNumOnWayLock()==null){
                        warehouseInStock.setNumOnWayLock(0);
                    }
                    goodsItemInventory
                            .setNumOnWayLock(goodsItemInventory.getNumOnWayLock() + warehouseInStock.getNumOnWayLock());
                }
            }
            // GoodsOnWayStockRpcDTO goodsOnWayStock = goodsOnWayStockMap.get(itemId);
            // if (goodsOnWayStock == null) {
            // LOGGER.info("下架-无在途库存信息，对当前货品:{}进行初始化,初始化为0",itemId);
            // goodsOnWayStock=new GoodsOnWayStockRpcDTO();
            // goodsOnWayStock.setNumLock(0);
            // goodsOnWayStock.setNumOnWay(0);
            // }
            // goodsItemInventory.setNumOnWay(goodsOnWayStock.getNumOnWay());
            GoodsVmiStockRpcDTO goodsVmiStock = goodsVmiStockMap.get(itemId);
            if (goodsVmiStock == null) {
                LOGGER.info("下架-无vmi库存，对当前货品:{}进行初始化,初始化为0", itemId);
                goodsVmiStock = new GoodsVmiStockRpcDTO();
                goodsVmiStock.setNumLock(0);
                goodsVmiStock.setNumVmi(0);
            }
            goodsItemInventory.setNumVmi(goodsVmiStock.getNumVmi());
            // 在库可下单量 在库数量+VMI数量-在库锁定-在库预留-VMI锁定-在途锁定
            Integer inStockCount = goodsItemInventory.getNumInWarehouse() + goodsVmiStock.getNumVmi()
                - goodsItemInventory.getNumInWarehouseLock() - goodsItemInventory.getNumReserved()
                - goodsVmiStock.getNumLock() - goodsItemInventory.getNumOnWayLock();
            goodsItemInventory.setInStockUsableCount(inStockCount);
            // 在途可下单量 =在库可下单量+在途数量
            goodsItemInventory.setOnWayUsableCount(inStockCount + goodsItemInventory.getNumOnWay());
            goodsItemInventory.setNumLock(
                goodsItemInventory.getNumInWarehouseLock() + goodsItemInventory.getNumOnWayLock() + goodsVmiStock.getNumLock());

            LOGGER.info("下架-当前商品id{},库存信息{}", itemId, JSONObject.toJSONString(goodsItemInventory));
            if (goodsItemInventory.getInStockUsableCount() <= 0 && goodsItemInventory.getOnWayUsableCount() <= 0) {
                willDown.add(itemId);
            }
        }
        // 进行下架
        if (willDown.size() > 0) {
            LOGGER.info("即将下架的货品:{}", willDown);
            goodsServiceRpc.updateGoodsItemSaleStatusByIds(willDown, GoodsItemConstant.SALE_STATUS_DOWN);
        }

    }

    @Override
    public void upperSaleStatus() {
        LOGGER.info("处理上架");
        // 找出下架的货品
        com.bat.dubboapi.goods.common.Response<List<StopGoodsItemRpcDTO>> goodsResponse =
            goodsServiceRpc.listLifeCycleStopGoodsItem(GoodsItemConstant.SALE_STATUS_DOWN);
        if (!goodsResponse.isSuccess() || goodsResponse.getData() == null) {
            return;
        }
        List<StopGoodsItemRpcDTO> stopGoodsItems = goodsResponse.getData();
        List<Integer> itemIds = stopGoodsItems.stream().map(StopGoodsItemRpcDTO::getId).collect(Collectors.toList());
        LOGGER.info("上架-查询到的货品ids:{}", itemIds);
        com.bat.dubboapi.warehouse.common.Response<WarehouseStockInfoRpcDTO> stockResponse =
            warehouseStockServiceRpc.getStockByItemIds(itemIds);
        if (stockResponse == null || stockResponse.getData() == null) {
            return;
        }
        WarehouseStockInfoRpcDTO warehouseInStockInfo = stockResponse.getData();
        LOGGER.info("上架-查询到的库存信息:{}", JSONObject.toJSONString(warehouseInStockInfo));
        Map<Integer, List<WarehouseInStockRpcDTO>> inStockGroup = new HashMap<>();
        // Map<Integer, GoodsOnWayStockRpcDTO> goodsOnWayStockMap;
        Map<Integer, GoodsVmiStockRpcDTO> goodsVmiStockMap;

        List<WarehouseInStockRpcDTO> warehouseInStockRpcDTOS = warehouseInStockInfo.getWarehouseInStockRpcDTOS();
        if (warehouseInStockRpcDTOS != null && warehouseInStockRpcDTOS.size() > 0) {
            inStockGroup =
                warehouseInStockRpcDTOS.stream().collect(Collectors.groupingBy(WarehouseInStockRpcDTO::getItemId));
        }

        // List<GoodsOnWayStockRpcDTO> goodsOnWayStocks = warehouseInStockInfo.getGoodsOnWayStockRpcDTOS();
        // if (goodsOnWayStocks == null) {
        // LOGGER.info("上架-在途库存为空指针，无法处理直接返回");
        // return;
        // }
        // goodsOnWayStockMap = goodsOnWayStocks.stream()
        // .collect(Collectors.toMap(GoodsOnWayStockRpcDTO::getItemId, a -> a, (k1, k2) -> k1));

        List<GoodsVmiStockRpcDTO> goodsVmiStocks = warehouseInStockInfo.getGoodsVmiStockRpcDTOS();
        if (goodsVmiStocks == null) {
            LOGGER.info("上架-VMI库存为空指针，无法处理直接返回");
            return;
        }
        goodsVmiStockMap =
            goodsVmiStocks.stream().collect(Collectors.toMap(GoodsVmiStockRpcDTO::getItemId, a -> a, (k1, k2) -> k1));

        List<Integer> willUpper = new ArrayList<>();
        for (Integer itemId : itemIds) {
            GoodsItemInventoryRpcDTO goodsItemInventory = new GoodsItemInventoryRpcDTO();
            List<WarehouseInStockRpcDTO> warehouseInStocks = inStockGroup.get(itemId);
            if (warehouseInStocks != null) {
                for (WarehouseInStockRpcDTO warehouseInStock : warehouseInStocks) {

                    if (warehouseInStock.getNumInWarehouse() == null) {
                        warehouseInStock.setNumInWarehouse(0);
                    }
                    goodsItemInventory.setNumInWarehouse(
                            goodsItemInventory.getNumInWarehouse() + warehouseInStock.getNumInWarehouse());

                    if (warehouseInStock.getNumReserved() == null) {
                        warehouseInStock.setNumReserved(0);
                    }
                    goodsItemInventory
                            .setNumReserved(goodsItemInventory.getNumReserved() + warehouseInStock.getNumReserved());

                    if (warehouseInStock.getNumInWarehouseLock() == null) {
                        warehouseInStock.setNumInWarehouseLock(0);
                    }
                    goodsItemInventory.setNumInWarehouseLock(
                            goodsItemInventory.getNumInWarehouseLock() + warehouseInStock.getNumInWarehouseLock());

                    if(warehouseInStock.getNumOnWayLock()==null){
                        warehouseInStock.setNumOnWayLock(0);
                    }

                    goodsItemInventory
                            .setNumOnWayLock(goodsItemInventory.getNumOnWayLock() + warehouseInStock.getNumOnWayLock());
                }
            }
            // GoodsOnWayStockRpcDTO goodsOnWayStock = goodsOnWayStockMap.get(itemId);
            // if (goodsOnWayStock == null) {
            // LOGGER.info("上架-无在途库存信息，对当前货品:{}进行初始化，初始化为0", itemId);
            // goodsOnWayStock = new GoodsOnWayStockRpcDTO();
            // goodsOnWayStock.setNumLock(0);
            // goodsOnWayStock.setNumOnWay(0);
            // }
            // goodsItemInventory.setNumOnWay(goodsOnWayStock.getNumOnWay());
            GoodsVmiStockRpcDTO goodsVmiStock = goodsVmiStockMap.get(itemId);
            if (goodsVmiStock == null) {
                LOGGER.info("上架-无vmi库存，对当前货品:{}进行初始化,初始化为0", itemId);
                goodsVmiStock = new GoodsVmiStockRpcDTO();
                goodsVmiStock.setNumLock(0);
                goodsVmiStock.setNumVmi(0);
            }
            goodsItemInventory.setNumVmi(goodsVmiStock.getNumVmi());
            // 在库可下单量 在库数量+VMI数量-在库锁定-在库预留-VMI锁定-在途锁定
            Integer inStockCount = goodsItemInventory.getNumInWarehouse() + goodsVmiStock.getNumVmi()
                - goodsItemInventory.getNumInWarehouseLock() - goodsItemInventory.getNumReserved()
                - goodsVmiStock.getNumLock() - goodsItemInventory.getNumOnWayLock();
            goodsItemInventory.setInStockUsableCount(inStockCount);
            // 在途可下单量 =在库可下单量+在途数量
            goodsItemInventory.setOnWayUsableCount(inStockCount + goodsItemInventory.getNumOnWay());
            goodsItemInventory.setNumLock(
                goodsItemInventory.getNumInWarehouseLock() + goodsItemInventory.getNumOnWayLock() + goodsVmiStock.getNumLock());
            LOGGER.info("上架-当前商品id{},库存信息{}", itemId, JSONObject.toJSONString(goodsItemInventory));
            if (goodsItemInventory.getInStockUsableCount() > 0 || goodsItemInventory.getOnWayUsableCount() > 0) {
                willUpper.add(itemId);
            }
        }
        // 进行上架
        if (willUpper.size() > 0) {
            // 改为上架状态
            LOGGER.info("即将上架的货品:{}", willUpper);
            goodsServiceRpc.updateGoodsItemSaleStatusByIds(willUpper, GoodsItemConstant.SALE_STATUS_UPPER);
        }

    }
}
