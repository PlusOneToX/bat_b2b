package com.bat.flexible.api.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductCategoryCmd {

    @ApiModelProperty(value = "类别id")
    private Integer id;
    
    @ApiModelProperty(value = "产品类别名称")
    @NotNull(message = "P_PRODUCT_CATEGORY_NAME_NULL")
    private String name;
    
    @ApiModelProperty(value = "产品英文")
    @NotNull(message = "P_PRODUCT_CATEGORY_ENGLISH_NAME_NULL")
    private String englishName;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;
    
    @ApiModelProperty(value = "备注")
    private String remark;
}
