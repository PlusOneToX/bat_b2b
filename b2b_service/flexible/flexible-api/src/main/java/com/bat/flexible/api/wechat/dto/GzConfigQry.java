package com.bat.flexible.api.wechat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "签名获取")
public class GzConfigQry {

    @ApiModelProperty(value = "url")
    @NotNull(message = "URL_NULL")
    private String url;

    @ApiModelProperty(value = "appId")
    private String appId;
}
