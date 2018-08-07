package com.bat.distributor.api.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/20 8:50
 */
@Data
@ApiModel(value = "UserLoginCmd", description = "分销商前台登录")
public class UserLoginQry {
    @NotBlank(message = "P_DISTRIBUTOR_USER_LOGIN_NAME_NULL")
    @ApiModelProperty(value = "用户名(登录名)", required = true, example = "bat")
    private String userName;
    @NotBlank(message = "P_DISTRIBUTOR_USER_LOGIN_PASSWORD_NULL")
    @ApiModelProperty(value = "登录密码(MD5加密)", required = true, example = "sfgregefqwew24356546523ffsf")
    private String password;

    @ApiModelProperty(value = "openId、小程序登录需填", required = true, example = "sfgregefqwew24356546523ffsf")
    private String openId;
}
