package com.bat.promotion.api.user.dto.customer.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "优惠券总数")
public class UserCustomerCouponCountDTO {

    @ApiModelProperty(value = "待领取")
    private  Integer unReceiveCount;

    @ApiModelProperty(value = "未使用")
    private  Integer unUsedCount;

    @ApiModelProperty(value = "已使用")
    private  Integer usedCount;

    @ApiModelProperty(value = "已过期")
    private  Integer expiredCount;



}