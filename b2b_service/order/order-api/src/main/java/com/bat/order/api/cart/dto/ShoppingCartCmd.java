package com.bat.order.api.cart.dto;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "购物车商品信息")
public class ShoppingCartCmd {

    @ApiModelProperty(value = "商品id", required = true, example = "1223324")
    @NotNull(message = "P_GOODS_ID_NULL")
    private Integer goodsId;
    @ApiModelProperty(value = "商品编码", required = true, example = "G183285435")
    @NotBlank(message = "P_GOODS_NO_NULL")
    private String goodsNo;
    @ApiModelProperty(value = "商品名称", required = true, example = "G183285435")
    @NotBlank(message = "P_GOODS_NAME_NULL")
    private String goodsName;
    @ApiModelProperty(value = "货品id", required = true, example = "123456")
    @NotNull(message = "P_GOODS_ITEM_ID_NULL")
    private Integer itemId;
    @ApiModelProperty(value = "货品编码", required = true, example = "80462336544")
    @NotBlank(message = "P_GOODS_ITEM_CODE_NULL")
    private String itemCode;
    @ApiModelProperty(value = "货品条码", required = true, example = "80462336544")
    private String barCode;
    @ApiModelProperty(value = "货品名称", required = true, example = "80462336544")
    @NotBlank(message = "P_GOODS_ITEM_NAME_NULL")
    private String itemName;
    @ApiModelProperty(value = "商品或货品图片", required = false, example = "80462336544")
    private String imageUrl;
    @ApiModelProperty(value = "货品规格", required = false, example = "80462336544")
    private String specsName;
    @ApiModelProperty(value = "货品颜色", required = false, example = "80462336544")
    private String colorName;
    @ApiModelProperty(value = "重量g", required = false, example = "10.23")
    private BigDecimal weight;
    @ApiModelProperty(value = "长度", required = false, example = "10.23")
    private BigDecimal length;
    @ApiModelProperty(value = "宽", required = false, example = "10.23")
    private BigDecimal width;
    @ApiModelProperty(value = "高", required = false, example = "10.23")
    private BigDecimal height;
    @ApiModelProperty(value = "商品类型 1-普通 2-定制", required = true, example = "1")
    @NotNull(message = "P_GOODS_TYPE_NULL")
    private Short goodsType;
    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制", required = false, example = "1")
    private Short diyType;
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
    @ApiModelProperty(value = "购物车定制信息(商品类型为2时必填)", required = false)
    private ShoppingCartDiyCmd diy;
}
