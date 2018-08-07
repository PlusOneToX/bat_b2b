package com.bat.warehouse.api.inStock.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class WarehouseStockQry {

    @NotNull(message = "W_ITEM_ID_LIST_NULL")
    @ApiModelProperty(value = "货品id列表")
    private List<Integer> itemIdList;

    @ApiModelProperty(value = "分销商归属销售区域id列表")
    private List<Integer> areaIdList;

    @ApiModelProperty(value = "分销商id列表")
    private Integer distributorId;

    @ApiModelProperty(value = "指定仓库id")
    private Integer warehouseId;
}
