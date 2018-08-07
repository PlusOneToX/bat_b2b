package com.bat.goods.api.scaleprice.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ScalePriceId", description = "价格等级id")
public class ScalePriceId {

    @NotNull(message = "P_GOODS_ID_NULL")
    @ApiModelProperty(value = "价格等级id", required = true, example = "12424")
    private Integer id;
}
