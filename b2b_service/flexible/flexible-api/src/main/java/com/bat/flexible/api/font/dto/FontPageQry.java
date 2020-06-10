package com.bat.flexible.api.font.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FontPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "字体编号/名称")
    private String content;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

}
