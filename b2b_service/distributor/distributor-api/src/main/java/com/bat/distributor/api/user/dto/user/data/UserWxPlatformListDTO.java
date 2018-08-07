package com.bat.distributor.api.user.dto.user.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "微信平台信息")
public class UserWxPlatformListDTO {
    @ApiModelProperty(value = "分销商微信平台id", example = "1111")
    private Integer id;
    @ApiModelProperty(value = "分销商平台编码", example = "bat")
    private String platform;
    @ApiModelProperty(value = "平台类型：1 公众号 2 小程序", example = "1")
    private Short type;
    @ApiModelProperty(value = "分销商微信平台名称", example = "bat")
    private String name;
    @ApiModelProperty(value = "微信平台appid", example = "bat")
    private String appId;
    @ApiModelProperty(value = "微信平台密钥", example = "bat")
    private String appSecret;

}