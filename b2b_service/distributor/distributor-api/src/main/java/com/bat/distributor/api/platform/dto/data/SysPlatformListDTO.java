package com.bat.distributor.api.platform.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商系统平台信息")
public class SysPlatformListDTO {
    @ApiModelProperty(value = "分销商系统平台id", example = "1111")
    private Integer id;
    @ApiModelProperty(value = "分销商平台名称", example = "bat")
    private String platformName;
    @ApiModelProperty(value = "分销商平台编码", example = "bat")
    private String platform;
    @ApiModelProperty(value = "平台appid", example = "bat")
    private String appId;
    @ApiModelProperty(value = "平台appKey", example = "bat")
    private String appKey;
    @ApiModelProperty(value = "分配的appSecret", example = "bat")
    private String appSecret;
    @ApiModelProperty(value = "域名或者IP", example = "bat")
    private String hostName;

}