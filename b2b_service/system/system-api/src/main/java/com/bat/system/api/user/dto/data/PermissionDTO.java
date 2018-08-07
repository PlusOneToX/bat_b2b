package com.bat.system.api.user.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/17 12:18
 */
@Data
@ApiModel(value = "PermissionDTO")
public class PermissionDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "服务名")
    private String service;
    @ApiModelProperty(value = "模块名")
    private String module;
    @ApiModelProperty(value = "权限名")
    private String permissionName;
    @ApiModelProperty(value = "权限英文名")
    private String permissionModule;
    @ApiModelProperty(value = "访问路径")
    private String urlPath;
    @ApiModelProperty(value = "访问方法")
    private String method;
    @ApiModelProperty(value = "排序值")
    private Integer sort;
    @ApiModelProperty(value = "状态")
    private Short status;
}
