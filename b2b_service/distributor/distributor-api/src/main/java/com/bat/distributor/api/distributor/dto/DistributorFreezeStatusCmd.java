package com.bat.distributor.api.distributor.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TradeOpenCmd", description = "分销商收款条件停用启用")
public class DistributorFreezeStatusCmd extends DistributorId {
    @NotNull(message = "P_DISTRIBUTOR_FREEZE_STATUS_NULL")
    @ApiModelProperty(value = "冻结状态 1,未冻结 2,冻结", required = false, example = "1")
    private Short freezeStatus;
}
