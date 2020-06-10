package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExchangeShareCarryOutCmd {

    @ApiModelProperty(value = "转发id")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;
}
