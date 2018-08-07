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
@ApiModel(value = "AccountId", description = "平台账户id")
public class AccountId {

    @NotNull(message = "P_ACCOUNT_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

}
