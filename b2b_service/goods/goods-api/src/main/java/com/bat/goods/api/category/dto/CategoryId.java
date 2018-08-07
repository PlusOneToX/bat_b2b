package com.bat.goods.api.category.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CategoryId", description = "品类id")
public class CategoryId {
    @NotNull(message = "P_GOODS_ID_NULL")
    @ApiModelProperty(value = "品类id", required = true, example = "1223324")
    private Integer id;
}
