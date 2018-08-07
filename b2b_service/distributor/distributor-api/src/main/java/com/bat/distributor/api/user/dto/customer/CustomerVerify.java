package com.bat.distributor.api.user.dto.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/20 8:50
 */
@Data
@ApiModel(value = "UserPhoneVerify", description = "C端客户手机号验证")
public class CustomerVerify {
    @NotBlank(message = "P_DISTRIBUTOR_CUSTOMER_PHONE_NULL")
    @ApiModelProperty(value = "手机号", required = true, example = "bat")
    private String phone;
    @NotNull(message = "P_DISTRIBUTOR_PHONE_CODE_TYPE_NULL")
    @ApiModelProperty(value = "验证码类型：验证码类型：1 注册申请 2 B端验证码修改密码 3 C端修改手机号 4 C端客户验证码登录 5 C端验证码修改密码", required = true,
        example = "bat")
    private Short codeType;
}
