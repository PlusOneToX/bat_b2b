package com.bat.warehouse.api.vmi;

import java.util.List;
import java.util.Set;

import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;

public interface GoodsVmiStockServiceI {
    GoodsVmiStockDO getByItemId(Integer itemId);

    void create(GoodsVmiStockDO goodsVmiStock);

    void update(GoodsVmiStockDO goodsVmiStockDO);

    void deleteByItemId(Integer itemId);

    /**
     * 只保存redis缓存、不保存mysql
     * 
     * @param goodsVmiStockDO
     */
    void updateRedisCacheOnly(GoodsVmiStockDO goodsVmiStockDO);

    /**
     * 批量修改mysql数据、不修改redis
     * 
     * @param goodsVmiStockDOList
     */
    void updateList(List<GoodsVmiStockDO> goodsVmiStockDOList);

    /**
     * 根据货品id查询VMI库存列表
     * 
     * @param itemIdSet
     * @return
     */
    List<GoodsVmiStockDO> listRedisByItemIdList(Set<Integer> itemIdSet);

    /**
     * 根据货品id查询VMI库存列表(包容性查询)
     * 
     * @param itemIdSet
     * @return
     */
    List<GoodsVmiStockDO> incListRedisByItemIdList(Set<Integer> itemIdSet);

    List<GoodsVmiStockDO> listByItemIds(List<Integer> itemIds);
}
