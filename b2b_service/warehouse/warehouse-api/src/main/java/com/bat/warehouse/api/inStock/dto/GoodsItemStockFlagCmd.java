package com.bat.warehouse.api.inStock.dto;

import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsItemStockFlagCmd implements Serializable {

    /**
     * 时间戳、没啥含义、用于做队列key
     */
    private Long time;

    private List<WarehouseDO> warehouseDOList;

    private List<GoodsItemStockFlagDetailCmd> detailCmdList;


}
