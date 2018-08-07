package com.bat.goods.api.goods.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GoodsId", description = "货品(SKU)id信息")
public class GoodsItemId {
    @NotNull(message = "P_GOODS_ID_NULL")
    @ApiModelProperty(value = "货品(SKU)id", required = true, example = "12424")
    private Integer id;
}
