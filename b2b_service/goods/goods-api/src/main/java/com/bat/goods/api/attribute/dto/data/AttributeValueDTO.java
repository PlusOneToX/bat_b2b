package com.bat.goods.api.attribute.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品属性值信息封装")
public class AttributeValueDTO {

    private Integer id;
    @ApiModelProperty(value = "商品属性名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "商品属性英文名称", required = false, example = "bat")
    private String nameEn;
    @ApiModelProperty(value = "排序,数据越小排在越前面", required = true, example = "1")
    private Integer sort;
}
