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
@ApiModel(value = "CouponStatusCmd", description = "优惠券状态更新")
public class CouponStatusCmd extends BaseId {
    @NotNull(message = "P_PROMOTION_STATUS_NULL")
    @ApiModelProperty(value = "状态：1 发布 4 提前结束(不影响用户已领取的优惠券) 5 已作废", required = true, example = "1")
    private Short couponStatus;
    @ApiModelProperty(value = "优惠券作废或提前结束说明", required = false, example = "1")
    private String invalidExplain;
}
