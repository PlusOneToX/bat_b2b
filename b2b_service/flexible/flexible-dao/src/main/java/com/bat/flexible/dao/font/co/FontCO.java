package com.bat.flexible.dao.font.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FontCO {

    @ApiModelProperty(value = "字体id")
    private Integer id;

    @ApiModelProperty(value = "字体名称")
    private String name;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "英文名称")
    private String englishName;

    @ApiModelProperty(value = "字体文件路径")
    private String fontFile;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;
}
