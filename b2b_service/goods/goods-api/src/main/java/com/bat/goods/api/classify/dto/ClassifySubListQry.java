package com.bat.goods.api.classify.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ClassifySubListQry", description = "根据停用启用状态获取商品分类列表")
public class ClassifySubListQry {
    @NotNull(message = "P_GOODS_ID_NULL")
    @ApiModelProperty(value = "商品分类id", required = true, example = "1223324")
    private Integer classifyId;
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = false, example = "0")
    private Short openFlag;
}
