package com.bat.flexible.api.material.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialRelaSimpleDTO {

    @ApiModelProperty(value = "不是材质id、是关联表id")
    private Integer id;

    @ApiModelProperty(value = "材质id")
    private Integer materialId;


    @ApiModelProperty(value = "材质名称",notes = "中文名称")
    private String name;

    @ApiModelProperty(value = "材质英文名称",notes = "英文")
    private String englishName;

    @ApiModelProperty(value = "产品类型中文名",notes = "中文")
    private String categoryName;

    @ApiModelProperty(value = "产品类型英文名称",notes = "英文")
    private String categoryEnglishName;

    @ApiModelProperty(value = "材质编码")
    private String materialNo;

    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "材质分类中文名",notes = "中文")
    private String parentName;

    @ApiModelProperty(value = "材质分类英文名称",notes = "英文")
    private String parentEnglishName;
}
