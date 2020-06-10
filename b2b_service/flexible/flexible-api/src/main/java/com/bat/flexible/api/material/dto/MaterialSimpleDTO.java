package com.bat.flexible.api.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MaterialSimpleDTO {

    @ApiModelProperty(value = "材质id")
    private Integer id;

    @ApiModelProperty(value = "材质名称")
    private String name;

    @ApiModelProperty(value = "材质英文名称")
    private String englishName;

    @ApiModelProperty(value = "产品类型id")
    private Integer categoryId;

    @ApiModelProperty(value = "材质父节点名称")
    private String parentName;

    @ApiModelProperty(value = "产品类型名称")
    private String categoryName;

    @ApiModelProperty(value = "产品类型英文名称")
    private String categoryEnglishName;

    @ApiModelProperty(value = "材质父节点英文名称")
    private String parentEnglishName;

    @ApiModelProperty(value = "材质父节点")
    private Integer parentId;

    @ApiModelProperty(value = "材质编码")
    private String materialNo;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;
}
