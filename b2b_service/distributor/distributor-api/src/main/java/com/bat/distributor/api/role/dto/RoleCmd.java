package com.bat.distributor.api.role.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "RoleAddCmd", description = "分销商联系人角色信息")
public class RoleCmd {

    @ApiModelProperty(value = "分销商联系人角色ID", required = false)
    private Integer id;
    @NotBlank(message = "P_DISTRIBUTOR_ROLE_NAME_NULL")
    @ApiModelProperty(value = "分销商联系人角色名称", required = true, example = "bat")
    private String name;
    @NotNull(message = "P_DISTRIBUTOR_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用0停用", required = true, example = "0")
    private Short openFlag;
}
