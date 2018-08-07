package com.bat.warehouse.manager.vmi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.bat.warehouse.manager.vmi.executor.GoodsVmiStockCmdExe;
import com.bat.warehouse.manager.vmi.executor.GoodsVmiStockQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.warehouse.api.vmi.GoodsVmiStockServiceI;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;

@Service
public class GoodsVmiStockServiceImpl implements GoodsVmiStockServiceI {

    @Autowired
    private GoodsVmiStockQryExe goodsVmiStockQryExe;

    @Autowired
    private GoodsVmiStockCmdExe goodsVmiStockCmdExe;

    @Override
    public GoodsVmiStockDO getByItemId(Integer itemId) {
        return goodsVmiStockQryExe.getByItemId(itemId);
    }

    @Transactional
    @Override
    public void create(GoodsVmiStockDO goodsVmiStock) {
        goodsVmiStock.setCreateTime(new Date());
        goodsVmiStock.setUpdateTime(new Date());
        goodsVmiStockCmdExe.create(goodsVmiStock);
    }

    @Transactional
    @Override
    public void update(GoodsVmiStockDO goodsVmiStockDO) {
        goodsVmiStockCmdExe.update(goodsVmiStockDO);
    }

    @Transactional
    @Override
    public void deleteByItemId(Integer itemId) {
        goodsVmiStockCmdExe.deleteByItemId(itemId);
    }

    /**
     * 只保存redis缓存、不保存mysql
     * 
     * @param goodsVmiStockDO
     */
    @Override
    public void updateRedisCacheOnly(GoodsVmiStockDO goodsVmiStockDO) {
        goodsVmiStockCmdExe.updateRedisCacheOnly(goodsVmiStockDO);
    }

    /**
     * 批量修改mysql数据、不修改redis
     * 
     * @param goodsVmiStockDOList
     */
    @Transactional
    @Override
    public void updateList(List<GoodsVmiStockDO> goodsVmiStockDOList) {
        goodsVmiStockCmdExe.updateList(goodsVmiStockDOList);
    }

    /**
     * 根据货品id查询VMI库存列表
     * 
     * @param itemIdSet
     * @return
     */
    @Override
    public List<GoodsVmiStockDO> listRedisByItemIdList(Set<Integer> itemIdSet) {
        return goodsVmiStockQryExe.listRedisByItemIdList(itemIdSet);
    }

    @Override
    public List<GoodsVmiStockDO> incListRedisByItemIdList(Set<Integer> itemIdSet) {
        return goodsVmiStockQryExe.incListRedisByItemIdList(itemIdSet);
    }

    @Override
    public List<GoodsVmiStockDO> listByItemIds(List<Integer> itemIds) {
        if (itemIds.size() == 0) {
            return new ArrayList<>();
        }
        return goodsVmiStockQryExe.listByItemIds(itemIds);
    }

}
