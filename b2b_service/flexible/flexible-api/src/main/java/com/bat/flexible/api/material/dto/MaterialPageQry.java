package com.bat.flexible.api.material.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MaterialPageQry extends BasePageParamQry {


    @ApiModelProperty(value = "材质类型id、产品分类id")
    private Integer categoryId;
    
    @ApiModelProperty(value = "状态 1、启用 0、禁用 不传查全部")
    private Short openFlag;

    @ApiModelProperty(value = "父id、顶级为0")
    private Integer parentId=0;

    
    @ApiModelProperty(value = "材质名称、编码")
    private String content;
    
}
