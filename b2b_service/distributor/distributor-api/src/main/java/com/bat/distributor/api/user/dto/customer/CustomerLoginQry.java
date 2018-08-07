package com.bat.distributor.api.user.dto.customer;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/7 8:58
 */
@Data
@ApiModel(description = "C端客户登录")
public class CustomerLoginQry {
    @NotBlank(message = "P_DISTRIBUTOR_CUSTOMER_PHONE_NULL")
    @ApiModelProperty(value = "手机号", required = true, example = "bat")
    private String phone;
    @NotBlank(message = "P_DISTRIBUTOR_CUSTOMER_LOGIN_PASSWORD_NULL")
    @ApiModelProperty(value = "登录密码(MD5加密)", required = true, example = "sfgregefqwew24356546523ffsf")
    private String password;
}
