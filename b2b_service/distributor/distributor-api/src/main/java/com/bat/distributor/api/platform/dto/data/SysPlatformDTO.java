package com.bat.distributor.api.platform.dto.data;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商系统平台信息")
public class SysPlatformDTO {
    @ApiModelProperty(value = "分销商系统平台id", example = "1111")
    private Integer id;
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

    @ApiModelProperty(value = "分销商列表")
    private List<PlatformDistributorDTO> distributors;

    @ApiModelProperty(value = "分销商接口列表")
    private List<SysPlatformApiDTO> apis;

}