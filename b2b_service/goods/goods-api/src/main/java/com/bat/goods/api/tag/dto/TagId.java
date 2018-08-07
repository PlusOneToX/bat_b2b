package com.bat.goods.api.tag.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ScalePriceId", description = "商品标签id")
public class TagId {

    @NotNull(message = "P_GOODS_ID_NULL")
    @ApiModelProperty(value = "商品标签id", required = true, example = "12424")
    private Integer id;
}
