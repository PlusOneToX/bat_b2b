package com.bat.system.api.user.dto;

import com.bat.system.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:49
 */
@Data
@ApiModel(value = "RoleQry", description = "角色查询")
public class RoleQry extends BaseSearchQry {

    @ApiModelProperty(value = "name", example = "管理员")
    private String name;
}
