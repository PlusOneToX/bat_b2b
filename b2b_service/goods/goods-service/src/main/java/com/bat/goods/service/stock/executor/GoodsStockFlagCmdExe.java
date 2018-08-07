package com.bat.goods.service.stock.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.service.common.Constant;
import com.bat.goods.service.goods.executor.GoodsQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.goods.api.goods.dto.GoodsItemSaleStatusCmd;
import com.bat.goods.dao.goods.dataobject.GoodsItemStatusDO;
import com.bat.goods.dao.stock.GoodsStockFlagDOMapper;
import com.bat.goods.dao.stock.dataobject.GoodsStockFlagDO;
import com.bat.goods.service.goods.convertor.GoodsConvertor;
import com.bat.goods.service.goods.executor.GoodsCmdExe;

@Component
public class GoodsStockFlagCmdExe {

    @Autowired
    private GoodsStockFlagDOMapper goodsStockFlagDOMapper;

    @Resource
    private GoodsQryExe goodsQryExe;

    @Resource
    private GoodsCmdExe goodsCmdExe;

    @Transactional(rollbackFor = Exception.class)
    public void batchCreate(List<GoodsStockFlagDO> goodsStockFlagDOList) {
        goodsStockFlagDOMapper.batchCreate(goodsStockFlagDOList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<GoodsStockFlagDO> updateList) {
        goodsStockFlagDOMapper.batchUpdate(updateList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByItemIdList(List<Integer> itemIdList) {
        goodsStockFlagDOMapper.deleteByItemIdList(itemIdList);
    }

    /**
     * 根据货品ids更新货品上下架状态
     *
     * @param itemIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void goodsItemSaleStatus(List<Integer> itemIds) {
        List<GoodsStockFlagDO> goodsStockFlagDOS = goodsStockFlagDOMapper.listByItemIds(itemIds);
        List<GoodsItemStatusDO> goodsItemStatusDOS =
            goodsQryExe.listGoodsItemStatusByItemIdsAndLifeCycle(itemIds, Constant.LIFE_CYCLE_5);
        List<GoodsItemSaleStatusCmd> saleStatusCmds =
            GoodsConvertor.toGoodsItemSaleStatusCmdListByGoodsStockFlagDOS(goodsStockFlagDOS, goodsItemStatusDOS);
        if (!CollectionUtils.isEmpty(saleStatusCmds)) {
            saleStatusCmds.forEach(saleStatusCmd -> {
                goodsCmdExe.saleStatusGoodsItem(saleStatusCmd);
            });
        }
    }
}
