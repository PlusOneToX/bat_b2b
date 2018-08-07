package com.bat.system.api.user.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:49
 */
@Data
@ApiModel(value = "RoleId", description = "角色id")
public class MenuIds {
    @NotNull(message = "P_ROLE_ID_NULL")
    @ApiModelProperty(value = "ids", required = true, example = "1")
    private List<Integer> ids;
}
