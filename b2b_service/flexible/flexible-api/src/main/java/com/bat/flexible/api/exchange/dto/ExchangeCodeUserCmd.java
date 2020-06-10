package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExchangeCodeUserCmd {

    @ApiModelProperty(value = "暗码")
    @NotBlank(message = "E_EXCHANGE_SECRET_CODE_NULL")
    private String secretCode;


    @ApiModelProperty(value = "C端用户id")
    @NotNull(message = "COMMON_USER_ID_NULL")
    private Integer userId;


}
