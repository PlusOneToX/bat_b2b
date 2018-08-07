package com.bat.system.api.user.dto;

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
@ApiModel(value = "UserStatusUpdateCmd", description = "后台用户状态更新")
public class UserStatusPasswordCmd {

    @NotNull(message = "P_USER_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    @NotNull(message = "P_USER_NEW_PASSWORD_NULL")
    @ApiModelProperty(value = "新密码", required = true, example = "1")
    private String newPassword;

    @NotNull(message = "P_USER_OLD_PASSWORD_NULL")
    @ApiModelProperty(value = "旧密码", required = true, example = "1")
    private String oldPassword;

}
