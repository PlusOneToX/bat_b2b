package com.bat.financial.api.refund.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 17:54
 */
@Data
@ApiModel(value = "RefundApplyId", description = "退款申请id")
public class RefundApplyId {
    @NotNull(message = "P_REFUND_APPLY_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;
}
