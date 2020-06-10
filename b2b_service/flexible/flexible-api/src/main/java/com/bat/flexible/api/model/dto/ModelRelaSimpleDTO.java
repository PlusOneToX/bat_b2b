package com.bat.flexible.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ModelRelaSimpleDTO {
    @ApiModelProperty(value = "关联id",notes = "不是型号id、是关联表id,该值不为空表示有关联关系")
    private Integer id;

    @ApiModelProperty(value = "型号id")
    private Integer modelId;


    @ApiModelProperty(value = "型号名称",notes = "中文名称")
    private String name;

    @ApiModelProperty(value = "型号英文名称",notes = "英文")
    private String englishName;

    @ApiModelProperty(value = "型号类别中文名",notes = "中文")
    private String categoryName;

    @ApiModelProperty(value = "型号类别英文名称",notes = "英文")
    private String categoryEnglishName;

    @ApiModelProperty(value = "型号编码")
    private String modelNo;

    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    private Short openFlag;
}
