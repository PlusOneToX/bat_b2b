package com.bat.distributor.api.role.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "RoleId", description = "分销商联系人角色id")
public class RoleId {

    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商联系人角色id", required = true, example = "12424")
    private Integer id;
}
