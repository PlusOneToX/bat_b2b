package com.bat.warehouse.dao.inStock.co;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel
public class GoodsItemInventoryCO {

    @ApiModelProperty(value = "在库库存id")
    private Integer id;

    @ApiModelProperty(value = "存货编码")
    private String itemCode;

    @ApiModelProperty(value = "货品id")
    private Integer itemId;

    @ApiModelProperty(value = "货品ERP的ID")
    private Integer itemErpId;

    @ApiModelProperty(value = "货品名称")
    private String itemName;

    @ApiModelProperty(value = "erp即时库存")
    private Integer erpNumInWarehouse;

    @ApiModelProperty(value = "在途数量")
    private Integer numOnWay;

    @ApiModelProperty(value = "VMI数量")
    private Integer numVmi;

    @ApiModelProperty(value = "在库可下单量")
    private Integer inStockUsableCount;

    @ApiModelProperty(value = "在途可下单量")
    private Integer onWayUsableCount;

    @ApiModelProperty(value = "预留数量")
    private Integer numReserved;

    @ApiModelProperty(value = "B2B锁定数量")
    private Integer numLock;

    @ApiModelProperty(value = "在库锁定数量")
    private Integer numInWarehouseLock;

    @ApiModelProperty(value = "在途锁定数量")
    private Integer numOnWayLock;

    @ApiModelProperty(value = "erp预计可发量 、也就是在库数量")
    private Integer numInWarehouse;
}
