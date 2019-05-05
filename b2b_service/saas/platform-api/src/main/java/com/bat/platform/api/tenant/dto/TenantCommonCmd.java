package com.bat.platform.api.tenant.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "公共配置添加或修改")
public class TenantCommonCmd {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @NotNull(message = "P_PLATFORM_TENANT_ID_NULL")
    @ApiModelProperty(value = "平台租户id")
    private Integer tenantId;

    @NotBlank(message = "P_PLATFORM_TENANT_NO_NULL")
    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @NotBlank(message = "P_PLATFORM_APP_SECRET_NULL")
    @ApiModelProperty(value = "分销微信小程序授权密钥")
    private String wxProgramAppSecret;

    @NotBlank(message = "P_PLATFORM_APPID_NULL")
    @ApiModelProperty(value = "分销微信小程序appid")
    private String wxProgramAppId;

    @ApiModelProperty(value = "兑换卡默认的分销商id")
    private Integer exchangeDistributorId;

    @ApiModelProperty(value = "主调色")
    private String colour;

}
