package com.bat.flexible.api.picture.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PictureThemeUrlDTO {

    @ApiModelProperty(value = "图片id")
    private Integer id;

    @ApiModelProperty(value = "主题链接")
    private String themeUrl;
}
