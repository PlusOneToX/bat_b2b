package com.bat.goods.api.classify.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ClassifyId", description = "商品分类id")
public class ClassifyId {
    @NotNull(message = "P_GOODS_ID_NULL")
    @ApiModelProperty(value = "商品分类id", required = true, example = "1223324")
    private Integer id;
}
