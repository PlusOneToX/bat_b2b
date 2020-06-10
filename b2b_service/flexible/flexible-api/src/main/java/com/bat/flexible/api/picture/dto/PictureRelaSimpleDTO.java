package com.bat.flexible.api.picture.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PictureRelaSimpleDTO {

    @ApiModelProperty(value = "关联id",notes = "不是图片id、是关联表id,该项有值表示有关联关系")
    private Integer id;

    @ApiModelProperty(value = "图片id")
    private Integer pictureId;


    @ApiModelProperty(value = "图片名称",notes = "中文名称")
    private String name;

    @ApiModelProperty(value = "图片英文名称",notes = "英文名称")
    private String englishName;

    @ApiModelProperty(value = "图片分类")
    private String categoryName;

    @ApiModelProperty(value = "图片分类",notes = "英文")
    private String categoryEnglishName;

    @ApiModelProperty(value = "状态",notes = "1、启用 0、禁用")
    private Short openFlag;
}
