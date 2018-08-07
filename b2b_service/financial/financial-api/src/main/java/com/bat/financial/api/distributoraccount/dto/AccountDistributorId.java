package com.bat.financial.api.distributoraccount.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 10:09
 */
@Data
@ApiModel(value = "AccountDistributorId", description = "平台账户id")
public class AccountDistributorId {

    @NotNull(message = "P_ACCOUNT_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "distributorId", required = true, example = "1")
    private String distributorId;

}
