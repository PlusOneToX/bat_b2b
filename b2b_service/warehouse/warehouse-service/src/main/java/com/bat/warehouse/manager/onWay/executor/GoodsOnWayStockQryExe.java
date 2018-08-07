package com.bat.warehouse.manager.onWay.executor;

import java.util.*;

import com.bat.warehouse.manager.common.constant.WarehouseKeyConstant;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.dao.onWay.GoodsOnWayStockDOMapper;
import com.bat.warehouse.dao.onWay.dataobject.GoodsOnWayStockDO;

@Component
public class GoodsOnWayStockQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsOnWayStockQryExe.class);

    @Autowired
    private GoodsOnWayStockDOMapper goodsOnWayStockDOMapper;

    @CreateCache(name = WarehouseKeyConstant.GOODS_ON_WAY_DO_PRE)
    private Cache<String, GoodsOnWayStockDO> onWayStockDOCache;

    /**
     * 根据货品id查询在途库存
     * 
     * @param itemId
     * @return
     */
    @Cached(name = WarehouseKeyConstant.GOODS_ON_WAY_DO_PRE, key = "#itemId")
    public GoodsOnWayStockDO findByItemId(Integer itemId) {
        if (itemId == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_NULL);
        }
        GoodsOnWayStockDO goodsOnWayStockDO = goodsOnWayStockDOMapper.findByItemId(itemId);
        if (goodsOnWayStockDO == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_ERROR);
        }
        return goodsOnWayStockDO;
    }

    public List<GoodsOnWayStockDO> listAll() {
        return goodsOnWayStockDOMapper.listAll();
    }

    /**
     * 根据货品id列表查询在途库存列表
     * 
     * @param itemIdSet
     * @return
     */
    public List<GoodsOnWayStockDO> listRedisByItemIdList(Set<Integer> itemIdSet) {
        if (itemIdSet == null || itemIdSet.size() == 0) {
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_NULL);
        }
        Set<String> itemIdKeySet = new HashSet<>();
        itemIdSet.stream().forEach(itemId -> {
            itemIdKeySet.add(TenantContext.getTenantNo() + ":" + itemId);
        });
        Map<String, GoodsOnWayStockDO> onWayStockDOCacheAll = onWayStockDOCache.getAll(itemIdKeySet);
        itemIdKeySet.stream().forEach(itemId -> {
            if (!onWayStockDOCacheAll.containsKey(itemId)) {
                LOGGER.error("货品id{}查询不到在途库存", itemId);
                throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_ERROR);
            }
        });
        return new ArrayList<>(onWayStockDOCacheAll.values());
    }

    public List<GoodsOnWayStockDO> incListRedisByItemIdList(Set<Integer> itemIdSet) {
        if (itemIdSet == null || itemIdSet.size() == 0) {
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_NULL);
        }
        Set<String> itemIdKeySet = new HashSet<>();
        itemIdSet.stream().forEach(itemId -> {
            itemIdKeySet.add(TenantContext.getTenantNo() + ":" + itemId);
        });
        Map<String, GoodsOnWayStockDO> onWayStockDOCacheAll = onWayStockDOCache.getAll(itemIdKeySet);
        if(onWayStockDOCacheAll==null){
            onWayStockDOCacheAll=new HashMap<>();
        }
        return new ArrayList<>(onWayStockDOCacheAll.values());
    }

    public List<GoodsOnWayStockDO> listByItemIds(List<Integer> itemIds) {
        return goodsOnWayStockDOMapper.findByItemIds(itemIds);
    }
}
