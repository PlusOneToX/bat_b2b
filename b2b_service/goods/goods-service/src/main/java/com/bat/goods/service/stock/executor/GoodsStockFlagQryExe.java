package com.bat.goods.service.stock.executor;

import com.bat.goods.dao.stock.GoodsStockFlagDOMapper;
import com.bat.goods.dao.stock.dataobject.GoodsStockFlagDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsStockFlagQryExe {

    @Autowired
    private GoodsStockFlagDOMapper goodsStockFlagDOMapper;

    public GoodsStockFlagDO getByItemIdAndWarehouseIdArr(Integer itemId, String warehouseId) {
        return goodsStockFlagDOMapper.getByItemIdAndWarehouseIdArr(itemId,warehouseId);
    }
}
