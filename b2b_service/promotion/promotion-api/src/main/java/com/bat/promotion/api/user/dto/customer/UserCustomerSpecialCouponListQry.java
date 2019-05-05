package com.bat.promotion.api.user.dto.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "优惠券列表查询")
public class UserCustomerSpecialCouponListQry {
    @NotNull(message = "P_PROMOTION_COUPON_STATUS_NULL")
    @ApiModelProperty(value = "领取状态：0 全部 1 待领取 2 已领取 3 未使用 4 已使用 5 已过期（多个中间用逗号隔开）", required = true, example = "0")
    private String statuss;
    @NotBlank(message = "P_PROMOTION_COUPON_TYPE_NULL")
    @ApiModelProperty(value = "优惠类型：1 普通, 4 新人 2 Note 20专题 3 S20 FE专题(多个中间用“,”隔开)", required = false, example = "2")
    private String couponTypes;

}