package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotNull;

import com.bat.distributor.api.base.BaseId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserCheckCmd", description = "分销商审核")
public class UserCheckCmd extends BaseId {

    @NotNull(message = "P_DISTRIBUTOR_USER_APPLY_STATUS_NULL")
    @ApiModelProperty(value = "审核状态 2审核通过 3审核拒绝 ", required = true, example = "bat")
    private Short applyStatus;
    @ApiModelProperty(value = "分销商价格等级id(checkStatus为2时，且等级为非默认价格等级时必填，默认价格等级不传)", required = false, example = "bat")
    private Integer scalePriceId;
}
