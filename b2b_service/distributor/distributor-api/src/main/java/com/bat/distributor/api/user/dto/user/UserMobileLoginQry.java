package com.bat.distributor.api.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "UserMobileLoginQry", description = "分销商前台手机号验证码登录")
public class UserMobileLoginQry extends UserPhoneVerifyCodeCmd {

    @ApiModelProperty(value = "openId")
    @NotBlank(message = "D_COMMON_OPENID_NULL")
    private String openId;


}
