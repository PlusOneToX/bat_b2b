package com.bat.warehouse.api.warehouseStockReserved.dto;

import com.bat.warehouse.api.base.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "WarehouseStockReservedPageQry",description = "货品库存预留分页查询")
public class WarehouseStockReservedPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "预留备注/业务单号",required = false)
    private String content;

    @ApiModelProperty(value = "预留状态 1、已处理 0、处理中",required = false)
    private Short status;

    @ApiModelProperty(value = "预留来源 1：手工添加，2：收单工具",required = false)
    private Short source;
    
}
