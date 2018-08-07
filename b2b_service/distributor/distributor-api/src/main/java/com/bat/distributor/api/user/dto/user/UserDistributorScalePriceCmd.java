package com.bat.distributor.api.user.dto.user;

import com.bat.distributor.api.base.BaseId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserCheckCmd", description = "分销商审核")
public class UserDistributorScalePriceCmd extends BaseId {
    @ApiModelProperty(value = "分销商价格等级id(checkStatus为2时，且等级为非默认价格等级时必填，默认价格等级不传)", required = false, example = "bat")
    private Integer scalePriceId;
}
