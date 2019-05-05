package com.bat.platform.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
@Data
public class UserLoginQry {

    @NotBlank(message = "P_PLATFORM_USER_NAME_NULL")
    @ApiModelProperty(value = "用户名(登录名)", required = true, example = "bat")
    private String userName;

    @NotBlank(message = "P_PLATFORM_PASSWORD_NULL")
    @ApiModelProperty(value = "登录密码(MD5加密)", required = true, example = "sfgregefqwew24356546523ffsf")
    private String password;


}
