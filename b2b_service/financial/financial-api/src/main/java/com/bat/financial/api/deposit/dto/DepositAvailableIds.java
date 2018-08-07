package com.bat.financial.api.deposit.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 20:15
 */
@Data
@ApiModel(value = "DepositAvailableIds", description = "预存款余额详情ids")
public class DepositAvailableIds {
    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "distributorIds", required = true, example = "1")
    List<Integer> ids;
}
