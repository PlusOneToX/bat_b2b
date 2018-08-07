package com.bat.distributor.api.role.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "RoleOpenCmd", description = "分销商联系人角色停用启用")
public class RoleOpenCmd extends RoleId {
    @NotNull(message = "P_DISTRIBUTOR_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
}
