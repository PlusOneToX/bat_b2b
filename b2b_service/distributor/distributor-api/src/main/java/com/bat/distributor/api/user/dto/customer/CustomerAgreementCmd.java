package com.bat.distributor.api.user.dto.customer;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 9:01
 */
@Data
@ApiModel(description = "C端客户协议确认")
public class CustomerAgreementCmd {
    @NotNull(message = "P_DISTRIBUTOR_CUSTOMER_ID_NULL")
    @ApiModelProperty(value = "客户id", example = "123444")
    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_CUSTOMER_AGREEMENT_NULL")
    @ApiModelProperty(value = "确认协议:1 已确认 0未确认", example = "0")
    private Short agreementFlag;

}
