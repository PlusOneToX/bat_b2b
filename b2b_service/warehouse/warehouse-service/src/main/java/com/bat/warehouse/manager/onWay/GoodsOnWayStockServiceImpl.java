package com.bat.warehouse.manager.onWay;

import com.bat.warehouse.api.onWay.GoodsOnWayStockServiceI;
import com.bat.warehouse.dao.onWay.dataobject.GoodsOnWayStockDO;
import com.bat.warehouse.manager.onWay.executor.GoodsOnWayStockCmdExe;
import com.bat.warehouse.manager.onWay.executor.GoodsOnWayStockQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GoodsOnWayStockServiceImpl implements GoodsOnWayStockServiceI {

    @Autowired
    private GoodsOnWayStockQryExe goodsOnWayStockQryExe;

    @Autowired
    private GoodsOnWayStockCmdExe goodsOnWayStockCmdExe;

    @Override
    public GoodsOnWayStockDO getByItemId(Integer itemId) {
        return goodsOnWayStockQryExe.findByItemId(itemId);
    }

    @Override
    public void create(GoodsOnWayStockDO goodsOnWayStockDO) {
        goodsOnWayStockCmdExe.create(goodsOnWayStockDO);
    }

    @Override
    public void update(GoodsOnWayStockDO goodsOnWayStockDO) {
        goodsOnWayStockCmdExe.update(goodsOnWayStockDO);
    }

    @Transactional
    @Override
    public void deleteByItemId(Integer itemId) {
        goodsOnWayStockCmdExe.deleteByItemId(itemId);
    }

    /**
     * 只保存redis、不保存到mysql
     * @param goodsOnWayStockDO
     */
    @Override
    public void updateRedisCacheOnly(GoodsOnWayStockDO goodsOnWayStockDO) {
        goodsOnWayStockCmdExe.updateRedisCacheOnly(goodsOnWayStockDO);
    }

    /**
     * 批量修改mysql、不更新redis
     * @param goodsOnWayStockDOList
     */
    @Transactional
    @Override
    public void updateList(List<GoodsOnWayStockDO> goodsOnWayStockDOList) {
        goodsOnWayStockCmdExe.updateList(goodsOnWayStockDOList);
    }

    /**
     * 根据货品id列表查询在途库存列表
     * @param itemIdSet
     * @return
     */
    @Override
    public List<GoodsOnWayStockDO> listRedisByItemIdList(Set<Integer> itemIdSet) {
        return goodsOnWayStockQryExe.listRedisByItemIdList(itemIdSet);
    }

    @Override
    public List<GoodsOnWayStockDO> incListRedisByItemIdList(Set<Integer> itemIdSet) {
        return goodsOnWayStockQryExe.incListRedisByItemIdList(itemIdSet);
    }


    @Override
    public void test() {
        System.out.println(111111111);
    }

    @Override
    public List<GoodsOnWayStockDO> listByItemIds(List<Integer> itemIds) {
        if (itemIds.size() == 0) {
            return new ArrayList<>();
        }
        return goodsOnWayStockQryExe.listByItemIds(itemIds);
    }
}
