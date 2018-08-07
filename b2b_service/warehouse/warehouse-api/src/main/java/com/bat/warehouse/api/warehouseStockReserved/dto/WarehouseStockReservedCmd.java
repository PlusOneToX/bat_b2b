package com.bat.warehouse.api.warehouseStockReserved.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class WarehouseStockReservedCmd {

    @ApiModelProperty(value = "仓库id")
    @NotNull(message = "W_WAREHOUSE_ID_NULL")
    private Integer warehouseId;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "预留明细")
    @NotEmpty(message = "W_RESERVED_DETAIL_NULL")
    private List<WarehouseStockReservedDetailCmd> detailCmdList;
}
