package com.bat.promotion.api.coupon.dto;

import javax.validation.constraints.NotNull;

import com.bat.promotion.api.base.BaseId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "优惠券信息")
public class CouponCountCmd extends BaseId {
    @NotNull(message = "P_PROMOTION_COUPON_GENERATE_COUNT_NULL")
    @ApiModelProperty(value = "优惠券总数量(填0不限制)", required = false, example = "10")
    private Integer generateCount;
    @NotNull(message = "P_PROMOTION_COUPON_LIMIT_COUNT_NULL")
    @ApiModelProperty(value = "每个客户限领数量(填0不限制)", required = false, example = "10")
    private Integer limitCount;
}
