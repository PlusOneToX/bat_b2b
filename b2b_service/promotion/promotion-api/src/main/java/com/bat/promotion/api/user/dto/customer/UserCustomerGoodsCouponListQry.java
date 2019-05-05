package com.bat.promotion.api.user.dto.customer;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.bat.promotion.api.base.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "优惠券列表查询")
public class UserCustomerGoodsCouponListQry extends BasePage {
    @NotNull(message = "P_PROMOTION_COUPON_STATUS_NULL")
    @ApiModelProperty(value = "领取状态：0 全部 1 待领取 2 已领取 3 未使用 4 已使用 5 已过期（多个中间用逗号隔开(注意：待领取不能和其他组合)，待领取不包含专题优惠券）",
        required = true, example = "1")
    private String statuss;

    @NotNull(message = "P_PROMOTION_GOODS_NULL")
    @Valid
    @ApiModelProperty(value = "商品列表", required = true)
    private List<UserCustomerGoodsItemQry> goodss;

}