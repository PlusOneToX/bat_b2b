package com.bat.flexible.api.model.dto.rela;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ModelMaterialRelevancePageQry extends BasePageParamQry {


    @ApiModelProperty(value = "材质名称/机型名称")
    private String content;

    @ApiModelProperty(value = "产品类型id")
    private Integer productCategoryId;

    @ApiModelProperty(value = "材质id")
    private Integer materialId;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;


}
