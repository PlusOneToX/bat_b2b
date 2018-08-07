package com.bat.warehouse.api.onWay;

import com.bat.warehouse.dao.onWay.dataobject.GoodsOnWayStockDO;

import java.util.List;
import java.util.Set;

public interface GoodsOnWayStockServiceI {
    GoodsOnWayStockDO getByItemId(Integer itemId);

    void create(GoodsOnWayStockDO goodsOnWayStockDO);

    void update(GoodsOnWayStockDO goodsOnWayStockDO);

    void deleteByItemId(Integer itemId);

    /**
     * 只保存redis缓存、不保存mysql
     * @param goodsOnWayStockDO
     */
    void updateRedisCacheOnly(GoodsOnWayStockDO goodsOnWayStockDO);

    /**
     * 批量修改mysql、不更新redis
     * @param goodsOnWayStockDOList
     */
    void updateList(List<GoodsOnWayStockDO> goodsOnWayStockDOList);

    /**
     * 根据货品id列表查询在途库存列表
     * @param itemIdSet
     * @return
     */
    List<GoodsOnWayStockDO> listRedisByItemIdList(Set<Integer> itemIdSet);

    /**
     * 根据货品id列表查询在途库存列表(包容性查询)
     * @param itemIdSet
     * @return
     */
    List<GoodsOnWayStockDO> incListRedisByItemIdList(Set<Integer> itemIdSet);

    void test();

    List<GoodsOnWayStockDO> listByItemIds(List<Integer> itemIds);
}
