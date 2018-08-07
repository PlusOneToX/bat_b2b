package com.bat.order.api.cart.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "购物车商品信息")
public class ShoppingCustomerCartCmd {

    @ApiModelProperty(value = "货品编码", required = true, example = "80462336544")
    @NotBlank(message = "P_GOODS_ITEM_CODE_NULL")
    private String itemCode;
    @ApiModelProperty(value = "是否赠品 1 非赠品 2 赠品", required = true, example = "1")
    @NotNull(message = "P_GOODS_ITEM_TYPE_NULL")
    private Short itemType;
    @ApiModelProperty(value = "商品促销活动Id(活动条件id)", required = false, example = "11252")
    private Integer goodsPromotionId;
    @ApiModelProperty(value = "订单促销活动Id(活动条件id)", required = false, example = "11252")
    private Integer orderPromotionId;
    @ApiModelProperty(value = "拼团秒杀活动id", required = false, example = "11252")
    private Integer groupSeckillId;
    @ApiModelProperty(value = "购物车数量", required = true, example = "123")
    @NotNull(message = "P_SHOPPING_CART_COUNT_NULL")
    private Integer itemCount;

    @ApiModelProperty(value = "货品单位")
    private String unit;

    @Valid
    @ApiModelProperty(value = "购物车定制信息", required = true)
    private ShoppingCartDiyCmd diy;
}
