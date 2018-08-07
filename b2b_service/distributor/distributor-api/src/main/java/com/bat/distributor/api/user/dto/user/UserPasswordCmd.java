package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商登录账号密码修改信息")
public class UserPasswordCmd {

    @ApiModelProperty(value = "账号id：当accountType=1时为分销商账号id(不能为空)，当accountType=2时手机号不能为空", required = false,
        example = "12323")
    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_CHANGE_TYPE_NULL")
    @ApiModelProperty(value = "修改方式：1 原密码修改 2 验证码修改", required = true, example = "1")
    private Short changeType;
    @ApiModelProperty(value = "原密码(当changeType为1时不能为空)", example = "1")
    private String oldPassword;
    @ApiModelProperty(value = "手机号(当changeType为2时不能为空)", example = "bat")
    private String phone;
    @ApiModelProperty(value = "验证码类型：验证码类型：1 注册申请 2 B端验证码修改密码 3 C端修改手机号 4 C端验证码修改密码(当changeType为2时不能为空)",
        example = "bat")
    private Short codeType;
    @ApiModelProperty(value = "验证码(当changeType为2时不能为空)", example = "bat")
    private String code;
    @NotBlank(message = "P_DISTRIBUTOR_NEW_PASSWORD_NULL")
    @ApiModelProperty(value = "新密码", required = true, example = "1")
    private String newPassword;
    @NotNull(message = "P_DISTRIBUTOR_ACCOUNT_TYPE_NULL")
    @ApiModelProperty(value = "登录账号类型：1 分销商主账号 2 分销商联系人账号", required = true, example = "1")
    private Short accountType;
}
