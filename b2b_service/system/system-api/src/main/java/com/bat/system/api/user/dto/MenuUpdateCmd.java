package com.bat.system.api.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "MenuUpdateCmd", description = "菜单更新")
public class MenuUpdateCmd {

    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "服务名")
    private String service;
    @ApiModelProperty(value = "服务名英文")
    private String serviceEn;
    @ApiModelProperty(value = "模块名")
    private String module;
    @ApiModelProperty(value = "模块名英文")
    private String moduleEn;
    @ApiModelProperty(value = "菜单名")
    private String menu;
    @ApiModelProperty(value = "菜单英文名")
    private String menuEn;
    @ApiModelProperty(value = "排序值")
    private Integer sort;
    @ApiModelProperty(value = "状态")
    private Short status;
}
