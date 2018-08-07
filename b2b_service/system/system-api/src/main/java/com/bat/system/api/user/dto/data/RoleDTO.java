package com.bat.system.api.user.dto.data;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "RoleDTO")
public class RoleDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "角色名")
    private String roleName;
    @ApiModelProperty(value = "角色英文名")
    private String roleNameEn;
    @ApiModelProperty(value = "角色描述")
    private String roleDescription;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "菜单列表")
    private List<MenuDTO> menus;
    @ApiModelProperty(value = "权限列表")
    private List<PermissionDTO> permissions;

}
