package com.bat.warehouse.manager.vmi.executor;

import java.util.*;

import com.bat.warehouse.manager.common.constant.WarehouseKeyConstant;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.dao.vmi.GoodsVmiStockDOMapper;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;

@Component
public class GoodsVmiStockQryExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsVmiStockQryExe.class);

    @Autowired
    private GoodsVmiStockDOMapper goodsVmiStockDOMapper;

    @CreateCache(name = WarehouseKeyConstant.GOODS_VMI_STOCK_DO_PRE)
    private Cache<String, GoodsVmiStockDO> vmiStockCache;

    @Cached(name = WarehouseKeyConstant.GOODS_VMI_STOCK_DO_PRE, key = "#itemId")
    public GoodsVmiStockDO getByItemId(Integer itemId) {
        if (itemId == null) {
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_NULL);
        }
        GoodsVmiStockDO goodsVmiStockDO = goodsVmiStockDOMapper.findByItemId(itemId);
        if (goodsVmiStockDO == null) {
            LOGGER.error("VMI仓库不存在货品id为:{}", itemId.toString());
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_ERROR);
        }
        return goodsVmiStockDO;
    }

    public List<GoodsVmiStockDO> listAll() {
        return goodsVmiStockDOMapper.listAll();
    }

    /**
     * 根据货品id查询VMI库存列表
     * 
     * @param itemIdSet
     * @return
     */
    public List<GoodsVmiStockDO> listRedisByItemIdList(Set<Integer> itemIdSet) {
        if (itemIdSet == null || itemIdSet.size() == 0) {
            LOGGER.error("查询redis的VMI库存、货品id列表为空{}", JSON.toJSONString(itemIdSet));
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_NULL);
        }
        Set<String> itemIdKeySet = new HashSet<>();
        itemIdSet.stream().forEach(itemId -> {
            itemIdKeySet.add(TenantContext.getTenantNo() + ":" + itemId);
        });
        Map<String, GoodsVmiStockDO> vmiStockCacheAll = vmiStockCache.getAll(itemIdKeySet);
        itemIdKeySet.stream().forEach(itemId -> {
            if (!vmiStockCacheAll.containsKey(itemId)) {
                LOGGER.error("货品id{}查询redis的VMI库存、数据为空", itemId);
                throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_ERROR);
            }
        });
        return new ArrayList<>(vmiStockCacheAll.values());
    }

    public List<GoodsVmiStockDO> incListRedisByItemIdList(Set<Integer> itemIdSet) {
        if (itemIdSet == null || itemIdSet.size() == 0) {
            LOGGER.error("查询redis的VMI库存、货品id列表为空{}", JSON.toJSONString(itemIdSet));
            throw new WarehouseException(WarehouseCommonErrorCode.ITEM_ID_NULL);
        }
        Set<String> itemIdKeySet = new HashSet<>();
        itemIdSet.stream().forEach(itemId -> {
            itemIdKeySet.add(TenantContext.getTenantNo() + ":" + itemId);
        });
        Map<String, GoodsVmiStockDO> vmiStockCacheAll = vmiStockCache.getAll(itemIdKeySet);
        if (vmiStockCacheAll == null) {
            vmiStockCacheAll = new HashMap<>();
        }
        return new ArrayList<>(vmiStockCacheAll.values());
    }

    public List<GoodsVmiStockDO> listByItemIds(List<Integer> itemIds) {
        return goodsVmiStockDOMapper.listByItemIds(itemIds);
    }

    /**
     * 根据已存在的货品id获取不存在的货品库存数据
     *
     * @param itemIds
     * @return
     */
    public List<GoodsVmiStockDO> getNoItemVmiStock(List<Integer> itemIds) {
        return goodsVmiStockDOMapper.getNoItemVmiStock(itemIds);
    }
}
