package com.bat.system.api.user.dto;

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
@ApiModel(value = "MenuId", description = "菜单id")
public class MenuId {
    @NotNull(message = "P_MENU_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "管理员")
    private Integer id;
}
