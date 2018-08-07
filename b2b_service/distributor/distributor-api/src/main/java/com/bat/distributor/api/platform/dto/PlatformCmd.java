package com.bat.distributor.api.platform.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PlatformCmd", description = "分销商平台信息")
public class PlatformCmd {

    @ApiModelProperty(value = "分销商平台id", required = false)
    private Integer id;
    @NotBlank(message = "P_DISTRIBUTOR_PLATFORM_NAME_NULL")
    @ApiModelProperty(value = "分销商平台名称", required = true, example = "bat")
    private String name;
    @NotBlank(message = "P_DISTRIBUTOR_PLATFORM_NO_NULL")
    @ApiModelProperty(value = "平台编码", required = true, example = "bat")
    private String platformNo;
    @NotNull(message = "P_DISTRIBUTOR_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用0停用", required = true, example = "0")
    private Short openFlag;
}
