package com.bat.financial.api.deposit.dto;

import java.util.List;

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
@ApiModel(value = "WithdrawalIds", description = "提现单编号Ids")
public class WithdrawalIds {
    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "提现单编号集合", example = "1")
    private List<Integer> ids;
}
