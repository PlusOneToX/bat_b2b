package com.bat.flexible.api.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductCategoryRelaDTO {

    @ApiModelProperty(value = "关联id",notes = "不是产品类别id、是关联表id")
    private Integer id;

    @ApiModelProperty(value = "产品类型id")
    private Integer productCategoryId;

    @ApiModelProperty(value = "产品类型名称",notes = "中文名称")
    private String name;

    @ApiModelProperty(value = "产品类型英文名称",notes = "英文")
    private String englishName;


}
