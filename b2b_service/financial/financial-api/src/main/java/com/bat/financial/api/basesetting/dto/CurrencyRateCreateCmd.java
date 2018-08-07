package com.bat.financial.api.basesetting.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/18 11:22
 */
@Data
@ApiModel(value = "CurrencyRateCreateCmd", description = "汇率新增")
public class CurrencyRateCreateCmd {

    @NotNull(message = "P_CURRENCY_RATE_EXCHANGE_RATE_NULL")
    @ApiModelProperty(value = "直接汇率", required = true, example = "6.5079")
    private BigDecimal exchangeRate;

    @NotNull(message = "P_CURRENCY_RATE_REVERSE_EX_RATE_NULL")
    @ApiModelProperty(value = "间接汇率", required = true, example = "0.1537")
    private BigDecimal reverseExRate;

    @NotBlank(message = "P_CURRENCY_RATE_CY_FOR_ID_NULL")
    @ApiModelProperty(value = "原币代码", required = true, example = "USD")
    private String cyForid;

    @NotBlank(message = "P_CURRENCY_RATE_CY_TO_ID_NULL")
    @ApiModelProperty(value = "目标币代码", required = true, example = "CNY")
    private String cyToid;

    @NotNull(message = "P_CURRENCY_RATE_ID_NULL")
    @ApiModelProperty(value = "开始时间", required = true, example = "2018-05-18 15:12:00")
    private Date begDate;

}
