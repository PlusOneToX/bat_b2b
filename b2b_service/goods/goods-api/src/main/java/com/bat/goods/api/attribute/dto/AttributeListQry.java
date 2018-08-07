package com.bat.goods.api.attribute.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AttributeListQry", description = "商品属性列表查询")
public class AttributeListQry {
    @ApiModelProperty(value = "查询内容，属性名称，支持模糊查询", required = false, example = "bat")
    private String content;
    @ApiModelProperty(value = "属性类型 1.货品规格 2.货品颜色 3.商品参数", required = false, example = "0")
    private Short attributeType;
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = false, example = "0")
    private Short openFlag;
    @NotNull(message = "P_GOODS_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_GOODS_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
