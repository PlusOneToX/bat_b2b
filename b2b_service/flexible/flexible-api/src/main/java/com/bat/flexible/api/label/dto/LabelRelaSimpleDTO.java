package com.bat.flexible.api.label.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LabelRelaSimpleDTO {

    @ApiModelProperty(value = "关联id",notes = "不是标签id、是关联表id")
    private Integer id;

    @ApiModelProperty(value = "标签id")
    private Integer labelId;


    @ApiModelProperty(value = "标签名称",notes = "中文名称")
    private String name;


    @ApiModelProperty(value = "标签类别",notes = "1、定制标签")
    private Short type;

    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    private Short openFlag;
}
