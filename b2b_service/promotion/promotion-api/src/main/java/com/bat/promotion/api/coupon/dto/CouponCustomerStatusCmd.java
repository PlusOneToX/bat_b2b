package com.bat.promotion.api.coupon.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(value = "CouponCustomerStatusCmd", description = "客户优惠券状态更新")
public class CouponCustomerStatusCmd {
    @NotNull(message = "P_PROMOTION_COUPON_NO_NULL")
    @ApiModelProperty(value = "优惠券券码(支持批量)", required = true, example = "1")
    private List<String> couponNos;
    @NotNull(message = "P_PROMOTION_STATUS_NULL")
    @ApiModelProperty(value = "状态：5 已作废", required = true, example = "1")
    private Short couponStatus;
    @ApiModelProperty(value = "优惠券作废或提前结束说明", required = false, example = "1")
    private String invalidExplain;
}
