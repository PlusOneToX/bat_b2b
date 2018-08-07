package com.bat.warehouse.dao.inStock.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class GoodsItemInventorySummaryCO {

    @ApiModelProperty(value = "在库库存id")
    private Integer id;

    @ApiModelProperty(value = "货品id")
    private Integer itemId;

    /**
     * ERP预计可发量(其实就是numInWarehouseSum、不是erp_num_in_warehouse)
     */
    @ApiModelProperty(value = "ERP即时库存（numInWarehouseSum）")
    private Integer erpNumInWarehouse;

    @ApiModelProperty(value = "在途数量")
    private Integer numOnWay;

    @ApiModelProperty(value = "VMI数量")
    private Integer numVmi;

    /**
     * 在库可下单量 = 在库数量+VMI数量-在库锁定-在库预留-VMI锁定-在途锁定
     *
     * -在途锁定 这个是有疑问的
     */
    @ApiModelProperty(value = "在库可下单量")
    private Integer inStockUsableCount;

    /**
     * 在途可下单量 =在库可下单量+在途数量
     */
    @ApiModelProperty(value = "在途可下单量")
    private Integer onWayUsableCount;

    @ApiModelProperty(value = "预留数量")
    private Integer numReservedSum;

    @ApiModelProperty(value = "ERP预计可发量（在库数量和）")
    private Integer numInWarehouseSum;

    @ApiModelProperty(value = "在途数量和")
    private Integer numOnWaySum;

    /**
     * B2B锁定数量 = 在库锁定和 + 在途锁定和 + VMI锁定和
     */
    @ApiModelProperty(value = "B2B锁定数量")
    private Integer numLockSum;

    @ApiModelProperty(value = "在库锁定和")
    private Integer numInWarehouseLockSum;

    @ApiModelProperty(value = "在途锁定和")
    private Integer numOnWayLockSum;

    @ApiModelProperty(value = "ERP锁定数量")
    private Integer erpNumLockSum;

}
