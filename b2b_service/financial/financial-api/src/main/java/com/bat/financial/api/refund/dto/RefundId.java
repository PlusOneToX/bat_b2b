package com.bat.financial.api.refund.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 18:13
 */
@Data
@ApiModel(value = "RefundId", description = "退款id")
public class RefundId {
    @NotNull(message = "P_REFUND_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;
}
