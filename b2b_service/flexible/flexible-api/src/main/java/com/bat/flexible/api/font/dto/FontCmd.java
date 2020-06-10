package com.bat.flexible.api.font.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FontCmd {

    @ApiModelProperty(value = "字体id")
    private Integer id;

    @ApiModelProperty(value = "字体名称")
    @NotBlank(message = "F_FONT_NAME_NULL")
    private String name;

    @ApiModelProperty(value = "文件名称")
    @NotBlank(message = "F_FONT_FILE_NAME_NULL")
    private String fileName;

    @ApiModelProperty(value = "英文名称")
    private String englishName;



    @ApiModelProperty(value = "字体文件路径")
    @NotBlank(message = "F_FONT_FONT_FILE_NULL")
    private String fontFile;

    @ApiModelProperty(value = "描述信息")
    private String description;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    @NotNull(message = "COMMON_OPEN_FLAG_NULL")
    private Short openFlag;
}
