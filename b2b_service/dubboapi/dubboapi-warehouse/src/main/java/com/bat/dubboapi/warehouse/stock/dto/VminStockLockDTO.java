package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VminStockLockDTO implements Serializable {
    private static final long serialVersionUID = -4010187478054753337L;
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
