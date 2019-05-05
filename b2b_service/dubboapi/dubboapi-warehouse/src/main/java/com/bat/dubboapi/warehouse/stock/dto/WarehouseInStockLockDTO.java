package com.bat.dubboapi.warehouse.stock.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseInStockLockDTO implements Serializable {

    private static final long serialVersionUID = -4271531798022353330L;
    /**
     * 库存id
     */
    private Integer stockId;
    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 在库锁定数量
     */
    private Integer numInWarehouseLock;

    /**
     * 在途锁定数量
     */
    private Integer numOnWayLock;
}
