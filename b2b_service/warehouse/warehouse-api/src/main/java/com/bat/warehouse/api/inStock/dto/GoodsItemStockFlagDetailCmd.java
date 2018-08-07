package com.bat.warehouse.api.inStock.dto;

import java.util.List;

import com.bat.warehouse.dao.inStock.dataobject.WarehouseInStockDO;
import com.bat.warehouse.dao.vmi.dataobject.GoodsVmiStockDO;

import lombok.Data;

@Data
public class GoodsItemStockFlagDetailCmd {

    /**
     * 在库与在途
     */
    private List<WarehouseInStockDO> warehouseInStockDOList;

    private Integer itemId;

    private GoodsVmiStockDO goodsVmiStockDO;
}
