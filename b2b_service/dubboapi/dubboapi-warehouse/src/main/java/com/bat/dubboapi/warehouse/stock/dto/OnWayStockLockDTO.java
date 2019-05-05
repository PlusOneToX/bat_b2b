package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OnWayStockLockDTO implements Serializable {
    private static final long serialVersionUID = -3503417795822543029L;
    /**
     * 库存id
     */
    private Integer stockId;

    /**
     * 仓库id
     */
    private Integer warehouseId;

    /**
     * 锁定的数量
     */
    private Integer lockNum;
}
