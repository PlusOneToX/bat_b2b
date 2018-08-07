package com.bat.financial.api.voucher.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/5 18:51
 */
@Data
@ApiModel(value = "VoucherId", description = "收款单id")
public class VoucherId {

    // @NotNull(message = "P_VOUCHER_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = false, example = "1")
    private Integer userId;

    @NotNull(message = "P_VOUCHER_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

}
