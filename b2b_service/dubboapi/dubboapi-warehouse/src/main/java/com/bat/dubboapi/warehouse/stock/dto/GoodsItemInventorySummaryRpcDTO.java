package com.bat.dubboapi.warehouse.stock.dto;


import lombok.Data;

import java.io.Serializable;


@Data
public class GoodsItemInventorySummaryRpcDTO implements Serializable {

    private static final long serialVersionUID = -183569457682524526L;
    /**
     * 在库库存id
     */
    private Integer id;


    /**
     * 货品id
     */
    private Integer itemId;

    /**
     * ERP预计可发量
     */
    private Integer erpNumInWarehouse;
    /**
     * 在途数量
     */
    private Integer numOnWay;
    /**
     * VMI数量
     */
    private Integer numVmi;
    /**
     * 在库可下单量
     */
    private Integer inStockUsableCount;
    /**
     * 在途可下单量
     */
    private Integer onWayUsableCount;
    /**
     * 预留数量
     */
    private Integer numReservedSum;
    /**
     * 锁定数量
     */
    private Integer numLockSum;
    /**
     * 在库数量和
     */
    private Integer numInWarehouseSum;
    /**
     * 在库锁定和
     */
    private Integer inStockNumLockSum;
    /**
     * ERP锁定数量
     */
    private Integer erpNumLockSum;
}
