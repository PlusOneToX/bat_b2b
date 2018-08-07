package com.bat.goods.api.attribute.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AttributeOpenCmd", description = "商品属性停用启用")
public class AttributeOpenCmd extends AttributeId {
    @NotNull(message = "P_GOODS_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = false, example = "0")
    private Short openFlag;
}
