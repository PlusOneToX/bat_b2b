package com.bat.goods.api.attribute.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AttributeCmd", description = "商品属性信息")
public class AttributeCmd {

    @ApiModelProperty(value = "商品属性ID", required = false, example = "1223324")
    private Integer id;
    @NotBlank(message = "P_GOODS_ATTRIBUTE_NAME_NULL")
    @ApiModelProperty(value = "商品属性名称,最长20个字符", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "商品属性英文名称，最长100个字符", required = false, example = "bat")
    private String nameEn;
    @ApiModelProperty(value = "商品属性描述", required = false, example = "bat品牌")
    private String description;
    @NotNull(message = "P_GOODS_ATTRIBUTE_TYPE_NULL")
    @ApiModelProperty(value = "属性类型 1.货品规格 2.货品颜色 3.商品参数", required = true, example = "0")
    private Short attributeType;
    @NotNull(message = "P_GOODS_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
    @Valid
    @ApiModelProperty("商品属性值")
    private List<AttributeValueCmd> values;

}
