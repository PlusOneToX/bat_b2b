package com.bat.distributor.api.trade.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TradeId", description = "分销商收款条件id")
public class TradeId {

    @NotNull(message = "P_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商收款条件id", required = true, example = "12424")
    private Integer id;
}
