package com.bat.system.api.user.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "UserLoginQry", description = "后台用户登录")
public class UserLoginQry {

    @NotBlank(message = "P_USER_NAME_NULL")
    @ApiModelProperty(value = "用户名", required = true, example = "张三")
    private String userName;

    @NotBlank(message = "P_USER_PASSWORD_NULL")
    @ApiModelProperty(value = "密码", required = true, example = "123465")
    private String password;
}
