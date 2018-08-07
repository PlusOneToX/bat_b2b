package com.bat.order.mq.dto;

import java.io.Serializable;

import lombok.Data;

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
