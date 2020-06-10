package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeSpecialDistributorUpdateCmd {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "状态 0停用 1启用")
    private Short status;
}
