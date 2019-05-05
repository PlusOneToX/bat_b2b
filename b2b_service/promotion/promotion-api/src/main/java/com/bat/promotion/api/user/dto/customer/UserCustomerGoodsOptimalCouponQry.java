package com.bat.promotion.api.user.dto.customer;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "优惠券列表查询")
public class UserCustomerGoodsOptimalCouponQry {

    @NotNull(message = "P_PROMOTION_GOODS_NULL")
    @Valid
    @ApiModelProperty(value = "商品列表", required = true)
    private List<UserCustomerGoodsItemQry> goodss;

}