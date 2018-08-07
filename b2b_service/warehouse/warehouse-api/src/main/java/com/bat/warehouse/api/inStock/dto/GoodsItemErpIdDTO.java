package com.bat.warehouse.api.inStock.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GoodsItemErpIdDTO {

    @ApiModelProperty(value = "ERP的货品id")
    @NotNull(message = "W_GOODS_ITEM_ERP_ID_NULL")
    private Long itemErpId;
}
