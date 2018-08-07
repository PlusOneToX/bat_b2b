package com.bat.financial.api.basesetting.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/18 14:15
 */
@Data
@ApiModel(value = "CurrencyRateId", description = "汇率id")
public class CurrencyRateId {
    @NotNull(message = "P_CURRENCY_RATE_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;
}
