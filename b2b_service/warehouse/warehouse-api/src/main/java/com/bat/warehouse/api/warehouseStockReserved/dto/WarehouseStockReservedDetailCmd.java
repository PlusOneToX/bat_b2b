package com.bat.warehouse.api.warehouseStockReserved.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class WarehouseStockReservedDetailCmd {


    @ApiModelProperty(value = "明细主键id、编辑时有值、新增空")
    private Integer id;

    @ApiModelProperty(value = "商品id")
    @NotNull(message = "W_GOODS_ID_NULL")
    private Integer goodsId;

    @ApiModelProperty(value = "货品id")
    @NotNull(message = "W_ITEM_ID_NULL")
    private Integer itemId;

    @ApiModelProperty(value = "货品名称")
    @NotBlank(message = "W_ITEM_NAME_NULL")
    private String itemName;

    @ApiModelProperty(value = "预留数量")
    @NotBlank(message = "W_NUM_RESERVED_NULL")
    @Min(message = "W_NUM_RESERVED_LESS_THEN_ONE",value = 1L)
    private Integer numReserved;
}
