package com.bat.distributor.api.trade.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TradeOpenCmd", description = "分销商收款条件停用启用")
public class TradeOpenCmd extends TradeId {
    @NotNull(message = "P_DISTRIBUTOR_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
}
