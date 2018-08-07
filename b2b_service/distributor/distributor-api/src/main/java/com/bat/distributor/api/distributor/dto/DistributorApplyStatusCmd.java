package com.bat.distributor.api.distributor.dto;

import javax.validation.constraints.NotNull;

import com.bat.distributor.api.base.BaseId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DistributorApplyStatusCmd", description = "分销商审核")
public class DistributorApplyStatusCmd extends BaseId {

    @NotNull(message = "P_DISTRIBUTOR_USER_APPLY_STATUS_NULL")
    @ApiModelProperty(value = "审核状态 2审核通过 3审核拒绝 ", required = true, example = "bat")
    private Short applyStatus;
}
