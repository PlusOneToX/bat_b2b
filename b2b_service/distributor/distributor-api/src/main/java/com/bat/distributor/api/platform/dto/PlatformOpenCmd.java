package com.bat.distributor.api.platform.dto;

import javax.validation.constraints.NotNull;

import com.bat.distributor.api.base.BaseId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PlatformOpenCmd", description = "分销商平台停用启用")
public class PlatformOpenCmd extends BaseId {
    @NotNull(message = "P_DISTRIBUTOR_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
}
