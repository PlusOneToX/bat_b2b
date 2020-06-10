package com.bat.flexible.api.picture.dto.theme;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PictureThemePageQry extends BasePageParamQry {

    @ApiModelProperty(value = "分销商名称或者主题名称")
    private String content;

    @ApiModelProperty(value = "状态1、启用 0、禁用")
    private Short openFlag;


}
