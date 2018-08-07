package com.bat.order.api.cart.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "购物车更新")
public class ShoppingCartUpdateCmd {

    @ApiModelProperty(value = "购物车id", required = true, example = "1223324")
    @NotNull(message = "P_CART_ID_NULL")
    private Integer id;
    @ApiModelProperty(value = "商品类型 1-普通 2-定制", required = true, example = "1")
    @NotNull(message = "P_GOODS_TYPE_NULL")
    private Short goodsType;
    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制", required = false, example = "1")
    private Short diyType;
    @ApiModelProperty(value = "是否赠品 1 非赠品 2 赠品", required = true, example = "1")
    @NotNull(message = "P_GOODS_ITEM_TYPE_NULL")
    private Short itemType;
    @ApiModelProperty(value = "购物车数量", required = true, example = "121")
    @NotNull(message = "P_SHOPPING_CART_COUNT_NULL")
    private Integer itemCount;
    @ApiModelProperty(value = "商品促销活动Id(活动条件id)", required = false, example = "11252")
    private Integer goodsPromotionId;
    @ApiModelProperty(value = "订单促销活动Id(活动条件id)", required = false, example = "11252")
    private Integer orderPromotionId;
    @ApiModelProperty(value = "拼团秒杀活动id", required = false, example = "11252")
    private Integer groupSeckillId;
}
