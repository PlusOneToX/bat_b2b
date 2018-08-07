package com.bat.distributor.api.customer.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 9:01
 */
@Data
@ApiModel(description = "C端客户冻结解冻")
public class CustomerStatusCmd {
    @NotNull(message = "P_DISTRIBUTOR_CUSTOMER_ID_NULL")
    @ApiModelProperty(value = "客户id", example = "123444")
    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_CUSTOMER_STATUS_NULL")
    @ApiModelProperty(value = "此用户是否已经被冻结 1为否,2为已冻结", example = "0")
    private Short status;

}
