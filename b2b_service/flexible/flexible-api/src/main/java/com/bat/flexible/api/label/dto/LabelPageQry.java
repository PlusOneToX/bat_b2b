package com.bat.flexible.api.label.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LabelPageQry")
public class LabelPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "类型",notes = "1、普通标签，2、定制标签")
    private Short type;

    @ApiModelProperty(value = "是否启用",notes = "1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "请输入标签名称",required = false)
    private String content;
}
