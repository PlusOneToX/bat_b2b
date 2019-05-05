package com.bat.platform.api.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "平台用户信息")
public class UserCmd {
    @ApiModelProperty(value = "平台用户ID", required = false)
    private Integer id;
    @NotBlank(message = "P_PLATFORM_USER_NAME_NULL")
    @ApiModelProperty(value = "账号，最长20个字符", required = true, example = "bat")
    private String userName;
    @NotBlank(message = "P_PLATFORM_PASSWORD_NULL")
    @ApiModelProperty(value = "用户登录密码(HD5加密)", required = true, example = "bat")
    private String password;
    @NotBlank(message = "P_PLATFORM_REAL_NAME_NULL")
    @ApiModelProperty(value = "姓名", required = true, example = "bat")
    private String realName;
    @NotBlank(message = "P_PLATFORM_MOBILE_NULL")
    @ApiModelProperty(value = "手机号", required = true, example = "bat")
    private String mobile;
    @ApiModelProperty(value = "邮箱", required = false, example = "bat@163.com")
    private String email;
    @NotNull(message = "P_PLATFORM_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1.启用 0.禁用", required = true, example = "1")
    private Short openFlag;
    @ApiModelProperty(value = "备注(可以填禁用原因)", required = false, example = "1u38383u3d")
    private String remark;
}