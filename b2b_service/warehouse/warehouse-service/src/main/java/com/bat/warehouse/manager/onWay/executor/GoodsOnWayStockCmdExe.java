package com.bat.warehouse.manager.onWay.executor;

import java.util.List;

import com.bat.warehouse.manager.common.constant.WarehouseKeyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.dao.onWay.GoodsOnWayStockDOMapper;
import com.bat.warehouse.dao.onWay.dataobject.GoodsOnWayStockDO;

@Component
public class GoodsOnWayStockCmdExe {

    @Autowired
    private GoodsOnWayStockDOMapper goodsOnWayStockDOMapper;

    @CreateCache(name = WarehouseKeyConstant.GOODS_ON_WAY_DO_PRE)
    private Cache<String, GoodsOnWayStockDO> cache;

    public void create(GoodsOnWayStockDO goodsOnWayStockDO) {
        goodsOnWayStockDOMapper.insert(goodsOnWayStockDO);
        cache.put(TenantContext.getTenantNo() + ":" + goodsOnWayStockDO.getItemId(), goodsOnWayStockDO);
    }

    @CacheUpdate(name = WarehouseKeyConstant.GOODS_ON_WAY_DO_PRE, key = "#goodsOnWayStockDO.itemId",
        value = "#goodsOnWayStockDO")
    public void update(GoodsOnWayStockDO goodsOnWayStockDO) {
        goodsOnWayStockDOMapper.updateByPrimaryKey(goodsOnWayStockDO);
    }

    public void deleteByItemId(Integer itemId) {
        goodsOnWayStockDOMapper.deleteByItemId(itemId);
        // 移除缓存
        GoodsOnWayStockDO goodsOnWayStockDO = cache.get(TenantContext.getTenantNo() + ":" + itemId);
        if (goodsOnWayStockDO != null) {
            cache.remove(TenantContext.getTenantNo() + ":" + itemId);
        }
    }

    public void updateRedisCacheOnly(GoodsOnWayStockDO goodsOnWayStockDO) {
        cache.put(TenantContext.getTenantNo() + ":" + goodsOnWayStockDO.getItemId(), goodsOnWayStockDO);
    }

    /**
     * 批量修改mysql、不更新redis
     * 
     * @param goodsOnWayStockDOList
     */
    public void updateList(List<GoodsOnWayStockDO> goodsOnWayStockDOList) {
        goodsOnWayStockDOMapper.updateList(goodsOnWayStockDOList);
    }
}
