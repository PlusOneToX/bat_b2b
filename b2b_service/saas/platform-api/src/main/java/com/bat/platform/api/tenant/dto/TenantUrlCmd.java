package com.bat.platform.api.tenant.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "租户url添加或修改")
public class TenantUrlCmd {

    @ApiModelProperty(value = "id")
    private Integer id;

    @NotNull(message = "P_PLATFORM_TENANT_ID_NULL")
    @ApiModelProperty(value = "平台租户id")
    private Integer tenantId;

    @NotBlank(message = "P_PLATFORM_TENANT_NO_NULL")
    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @NotNull(message = "P_PLATFORM_URL_TYPE_NULL")
    @ApiModelProperty(value = "1-分销后台PC端 2-分销前台PC端 3-分销前台H5端 4-店铺二维码 5-分销商申请二维码 6-后端接口 7 柔性H5端")
    private Short urlType;

    @NotBlank(message = "P_PLATFORM_HOST_NULL")
    @ApiModelProperty(value = "主机host")
    private String host;

    @NotBlank(message = "P_PLATFORM_URL_NULL")
    @ApiModelProperty(value = "访问url")
    private String url;

}
