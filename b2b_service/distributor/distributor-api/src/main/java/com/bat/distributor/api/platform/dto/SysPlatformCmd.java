package com.bat.distributor.api.platform.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商系统平台信息")
public class SysPlatformCmd {
    @ApiModelProperty(value = "分销商系统平台id", required = false, example = "1111")
    private Integer id;
    @NotBlank(message = "P_DISTRIBUTOR_PLATFORM_NO_NULL")
    @ApiModelProperty(value = "分销商平台编码", required = true, example = "bat")
    private String platform;
    @ApiModelProperty(value = "平台appid", required = true, example = "bat")
    private String appId;
    @ApiModelProperty(value = "平台appKey", required = true, example = "bat")
    private String appKey;
    @ApiModelProperty(value = "平台密钥", required = true, example = "bat")
    private String appSecret;
    @NotBlank(message = "P_DISTRIBUTOR_HOST_NAME_NULL")
    @ApiModelProperty(value = "域名或者IP", required = true, example = "bat")
    private String hostName;

    @Valid
    @NotNull(message = "P_DISTRIBUTOR_LIST_NULL")
    @ApiModelProperty(value = "分销商列表", required = true)
    private List<PlatformDistributorCmd> distributors;

    @Valid
    @NotNull(message = "P_DISTRIBUTOR_API_NULL")
    @ApiModelProperty(value = "分销商接口列表", required = true)
    private List<SysPlatformApiCmd> apis;

}