package com.bat.platform.api.tenant.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "租户url查询")
public class TenantUrlQry {

    @ApiModelProperty(
        value = "传参主机类型(传参主机host不为空是填)：1-分销后台PC端 2-分销前台PC端 3-分销前台H5端 4-店铺二维码 5-分销商申请二维码 6-后端接口 7 柔性H5端",
        required = false, example = "1")
    private Short qryUrlType;

    @ApiModelProperty(value = "传参主机host", required = false, example = "")
    private String host;

    @ApiModelProperty(value = "租户编码", required = false, example = "100")
    private String tenantNo;

    @ApiModelProperty(value = "租户对应分销小程序appid", required = false, example = "100")
    private String wxProgramAppId;

    @NotNull(message = "P_PLATFORM_URL_TYPE_NULL")
    @ApiModelProperty(
        value = "需获取的主机类型：1-分销后台PC端 2-分销前台PC端 3-分销前台H5端 4-店铺二维码 5-分销商申请二维码 6-后端接口 7 柔性H5端",
        required = true, example = "1")
    private Short gainUrlType;

}
