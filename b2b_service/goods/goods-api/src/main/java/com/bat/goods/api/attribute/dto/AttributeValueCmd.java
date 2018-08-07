package com.bat.goods.api.attribute.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品属性值信息封装")
public class AttributeValueCmd {

    @ApiModelProperty(value = "商品属性值Id,当operationType=2或3时，不能为空", required = true, example = "bat")
    private Integer id;
    @NotBlank(message = "P_GOODS_ATTRIBUTE_VALUE_NAME_NULL")
    @ApiModelProperty(value = "商品属性值名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "商品属性值英文名称", required = false, example = "bat")
    private String nameEn;
    @NotNull(message = "P_GOODS_SORT_NULL")
    @ApiModelProperty(value = "排序,数据越小排在越前面", required = true, example = "1")
    private Integer sort;
    @NotNull(message = "P_GOODS_OPERATION_TYPE")
    @ApiModelProperty(value = "操作类型,1.新增 2.修改 3.删除", required = true, example = "1")
    private Short operationType;
}
