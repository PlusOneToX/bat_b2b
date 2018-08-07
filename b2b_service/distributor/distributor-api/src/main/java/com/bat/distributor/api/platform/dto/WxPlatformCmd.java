package com.bat.distributor.api.platform.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商微信平台信息")
public class WxPlatformCmd {
    @ApiModelProperty(value = "分销商微信平台id", required = false, example = "1111")
    private Integer id;
    @NotBlank(message = "P_DISTRIBUTOR_PLATFORM_NO_NULL")
    @ApiModelProperty(value = "分销商平台编码", required = true, example = "bat")
    private String platform;
    @NotNull(message = "P_DISTRIBUTOR_PLATFORM_TYPE_NULL")
    @ApiModelProperty(value = "平台类型：1 公众号 2 小程序", required = true, example = "1")
    private Short type;
    @NotBlank(message = "P_DISTRIBUTOR_PLATFORM_NAME_NULL")
    @ApiModelProperty(value = "分销商微信平台名称", required = true, example = "bat")
    private String name;
    @NotBlank(message = "P_DISTRIBUTOR_APP_ID_NULL")
    @ApiModelProperty(value = "微信平台appid", required = true, example = "bat")
    private String appId;
    @NotBlank(message = "P_DISTRIBUTOR_APP_SECRET_NULL")
    @ApiModelProperty(value = "微信平台密钥", required = true, example = "bat")
    private String appSecret;

    @Valid
    @NotNull(message = "P_DISTRIBUTOR_LIST_NULL")
    @ApiModelProperty(value = "分销商列表", required = true)
    private List<PlatformDistributorCmd> distributors;

}