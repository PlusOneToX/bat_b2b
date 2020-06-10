package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExchangeSpecialDistributorAddCmd {

    @ApiModelProperty(value = "分销商id")
    private List<Integer> distributorIds;

    @ApiModelProperty(value = "专题id")
    private Integer exchangeSpecialId;
}
