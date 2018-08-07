package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商修改手机号信息")
public class UserChangePhoneCmd {

    @NotBlank(message = "P_DISTRIBUTOR_OLD_PHONE_NULL")
    @ApiModelProperty(value = "原手机号", required = true, example = "1")
    private String oldPhone;
    @NotBlank(message = "P_DISTRIBUTOR_NEW_PHONE_NULL")
    @ApiModelProperty(value = "新手机号", required = true, example = "1")
    private String newPhone;
    @NotBlank(message = "P_DISTRIBUTOR_USER_PHONE_CODE_NULL")
    @ApiModelProperty(value = "验证码", required = true, example = "bat")
    private String code;
    @NotNull(message = "P_DISTRIBUTOR_USER_PHONE_CODE_TYPE_NULL")
    @ApiModelProperty(value = "验证码类型：1 注册申请 2 忘记密码 3 修改手机号", required = true, example = "bat")
    private Short codeType;
}
