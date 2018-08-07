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
@ApiModel(value = "UserId", description = "后台用户id")
public class UserId {
    @NotNull(message = "P_USER_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;
}
