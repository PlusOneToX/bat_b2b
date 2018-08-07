package com.bat.financial.api.basesetting.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 13:54
 */
@Data
@ApiModel(value = "CurrencyQry", description = "币别查询")
public class CurrencyQry {
    @NotNull(message = "P_CURRENCY_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;

    @NotNull(message = "P_CURRENCY_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
