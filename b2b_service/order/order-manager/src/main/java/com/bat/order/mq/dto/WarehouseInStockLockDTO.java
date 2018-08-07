package com.bat.order.mq.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class WarehouseInStockLockDTO implements Serializable {

    /**
     * 库存id
     */
    private Integer stockId;
    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 该仓库锁定的在库数量
     */
    private Integer numInWarehouseLock;

    /**
     * 该仓库锁定的在途数量
     */
    private Integer numOnWayLock;
}
