package com.bat.financial.api.deposit.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/7 11:47
 */
@Data
@ApiModel(value = "DepositAvailableId", description = "预存款余额详情id")
public class DepositAvailableId {
    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "distributorId", required = true, example = "1")
    Integer id;
}
