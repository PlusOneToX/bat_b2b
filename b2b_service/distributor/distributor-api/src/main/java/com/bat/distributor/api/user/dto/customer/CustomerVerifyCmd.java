package com.bat.distributor.api.user.dto.customer;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/20 8:50
 */
@Data
@ApiModel(value = "CustomerVerifyLoginCmd", description = "C端客户验证码")
public class CustomerVerifyCmd extends CustomerVerify {
    @NotBlank(message = "P_DISTRIBUTOR_USER_PHONE_CODE_NULL")
    @ApiModelProperty(value = "验证码", required = true, example = "bat")
    private String code;
}
