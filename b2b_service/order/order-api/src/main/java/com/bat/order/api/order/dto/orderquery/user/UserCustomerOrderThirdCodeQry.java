package com.bat.order.api.order.dto.orderquery.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "第三方兑换码")
public class UserCustomerOrderThirdCodeQry {

    @NotBlank(message = "P_ORDER_THIRD_CODE_NAME_NULL")
    @ApiModelProperty(value = "第三方兑换码", required = true)
    private String code;

}
