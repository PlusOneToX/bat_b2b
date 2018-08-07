package com.bat.financial.api.deposit.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/10 16:58
 */
@Data
@ApiModel(value = "DistributorId", description = "分销商id")
public class DistributorId {
    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商id", example = "1")
    private Integer id;
}
