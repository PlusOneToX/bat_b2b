package com.bat.distributor.api.user.dto.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 9:01
 */
@Data
@ApiModel(description = "C端客户修改密码")
public class CustomerPasswordCmd {
    @NotNull(message = "P_DISTRIBUTOR_CUSTOMER_ID_NULL")
    @ApiModelProperty(value = "客户id", example = "123444")
    private Integer id;
    @NotBlank(message = "P_DISTRIBUTOR_CUSTOMER_OLD_PASSWORD_NULL")
    @ApiModelProperty(value = "旧密码", example = "18650811111")
    private String oldPassword;
    @NotBlank(message = "P_DISTRIBUTOR_CUSTOMER_NEW_PASSWORD_NULL")
    @ApiModelProperty(value = "新密码", example = "18650811111")
    private String newPassword;

}
