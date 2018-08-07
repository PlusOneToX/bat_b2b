package com.bat.order.mq.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class OrderUnStockLockDTO implements Serializable {
    /**
     * 锁库的货品id
     */
    private Integer itemId;

    /**
     * VMI锁定
     */
    private VminStockLockDTO vmiLock;

    /**
     * 在库库存锁定列表
     */
    private List<WarehouseInStockLockDTO> onWayLockDTOList;

    /**
     * 在库库存锁定列表
     */
    private List<WarehouseInStockLockDTO> inStockLockDTOList;
}
