package com.bat.flexible.api.product.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;


@Data
@ApiModel
public class ProductCategoryPageQry extends BasePageParamQry {


    @ApiModelProperty(value = "产品类型名称/英文名称")
    private String content;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

}
