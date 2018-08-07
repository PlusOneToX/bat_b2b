package com.bat.order.api.stock.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderGoodsStockQry {

    @ApiModelProperty(value = "仓库id")
    @NotNull(message = "O_WAREHOUSE_ID_NULL")
    private Integer warehouseId;

    @ApiModelProperty(value = "货品id")
    @NotNull(message = "GOODS_ITEM_ID_NULL")
    private Integer itemId;

    @ApiModelProperty(value = "锁定类型 1.在库 2.在途 3.vmi")
    private Short lockType;
}
