package com.bat.system.api.user.dto;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "RoleUpdateCmd", description = "角色更新")
public class RoleUpdateCmd {

    @NotNull(message = "P_ROLE_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    @NotBlank(message = "P_ROLE_NAME_NULL")
    @ApiModelProperty(value = "角色名称", required = true, example = "管理员")
    private String roleName;

    @ApiModelProperty(value = "角色英文名称")
    private String roleNameEn;

    @ApiModelProperty(value = "角色描述")
    private String roleDescription;

    @ApiModelProperty(value = "权限Ids")
    private List<Integer> permissionIds;

    @ApiModelProperty(value = "菜单Ids")
    private List<Integer> menusIds;

    @ApiModelProperty(value = "权限module(兼容旧接口)")
    private List<Map<String, String>> permissions;

    @ApiModelProperty(value = "菜单module(兼容旧接口)")
    private List<Map<String, String>> menus;

}
