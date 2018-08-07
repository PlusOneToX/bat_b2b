package com.bat.goods.api.brand.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BrandId", description = "品牌属性id")
public class BrandId {
    @NotNull(message = "P_GOODS_ID_NULL")
    @ApiModelProperty(value = "品牌属性id", required = true, example = "1223324")
    private Integer id;
}
