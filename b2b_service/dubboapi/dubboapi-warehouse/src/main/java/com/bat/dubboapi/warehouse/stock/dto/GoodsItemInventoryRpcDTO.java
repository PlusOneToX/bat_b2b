package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class GoodsItemInventoryRpcDTO implements Serializable {

    /**
     * 在库库存id
     */
    private Integer id;


    /**
     * 存货编码
     */
    private String itemCode;

    /**
     * 货品id
     */
    private Integer itemId;

    private Integer itemErpId;

    private String itemName;

    /**
     * erp即时库存
     */
    private Integer erpNumInWarehouse=0;


    /**
     * 在途数量
     */
    private Integer numOnWay=0;

    /**
     * VMI数量
     */
    private Integer numVmi=0;

    /**
     * 在库可下单量
     */
    private Integer inStockUsableCount=0;

    /**
     * 在途可下单量
     */
    private Integer onWayUsableCount=0;

    /**
     * 预留数量
     */
    private Integer numReserved=0;

    /**
     * B2B锁定数量 在库锁定 在途锁定 VMI锁定
     */
    private Integer numLock;

    /**
     * 在库锁定数量
     */
    private Integer numInWarehouseLock=0;

    /**
     * 在途锁定数量
     */
    private Integer numOnWayLock=0;

    /**
     * erp预计可发量 、也就是在库数量
     */
    private Integer numInWarehouse=0;
}
