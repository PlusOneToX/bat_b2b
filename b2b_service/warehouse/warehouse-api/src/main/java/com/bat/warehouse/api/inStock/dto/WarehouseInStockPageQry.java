package com.bat.warehouse.api.inStock.dto;

import com.bat.warehouse.api.base.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "WarehouseInStockPageQry",description = "货品库存分页查询")
public class WarehouseInStockPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "存货编码/存货名称",required = false)
    private String content;

    @ApiModelProperty(value = "仓库id",required = false)
    @NotNull(message = "W_WAREHOUSE_ID_NULL")
    private Integer warehouseId;
    
}
