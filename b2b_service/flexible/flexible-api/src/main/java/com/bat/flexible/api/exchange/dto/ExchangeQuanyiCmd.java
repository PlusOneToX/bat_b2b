package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeQuanyiCmd {

    @ApiModelProperty(value = "第三方编码")
    private String thirdCode;

    @ApiModelProperty(value = "C端客户编号")
    private String customerNo;

    @ApiModelProperty(value = "c端用户id")
    private Integer userId;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;
}
