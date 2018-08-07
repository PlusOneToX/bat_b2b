package com.bat.goods.api.attribute.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AttributeId", description = "商品属性id")
public class AttributeId {
    @NotNull(message = "P_GOODS_ID_NULL")
    @ApiModelProperty(value = "商品属性id", required = true, example = "1223324")
    private Integer id;
}
