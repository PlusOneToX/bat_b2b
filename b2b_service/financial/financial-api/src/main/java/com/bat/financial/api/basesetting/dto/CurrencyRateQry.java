package com.bat.financial.api.basesetting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 13:54
 */
@Data
@ApiModel(value = "CurrencyRateQry", description = "汇率查询")
public class CurrencyRateQry {

    @ApiModelProperty(value = "id", required = false, example = "1")
    private Integer id;

    @ApiModelProperty(value = "原币代码", required = false, example = "USD")
    private String cyForid;

    @ApiModelProperty(value = "目标币代码", required = false, example = "CNY")
    private String cyToid;
}
