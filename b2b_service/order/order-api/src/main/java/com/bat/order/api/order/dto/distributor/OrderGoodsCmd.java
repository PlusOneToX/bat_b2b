package com.bat.order.api.order.dto.distributor;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "订单商品明细信息")
public class OrderGoodsCmd {
    @ApiModelProperty(value = "货品编码", required = true, example = "123456")
    @NotBlank(message = "P_GOODS_ITEM_CODE_NULL")
    private String itemCode;
    @ApiModelProperty(value = "是否赠品 1 非赠品 2 赠品", required = true, example = "1")
    @NotNull(message = "P_GOODS_ITEM_TYPE_NULL")
    private Short itemType;
    @ApiModelProperty(value = "是否预售 1 是 0 否", required = true, example = "1")
    @NotNull(message = "P_GOODS_ITEM_MTO_TYPE_NULL")
    private Short mtoType;
    @ApiModelProperty(value = "是否支持超卖 1、支持 0 不支持", required = true, example = "0")
    @NotNull(message = "P_GOODS_OVERSOLD_FLAG_NULL")
    private Short oversoldFlag;
    @ApiModelProperty(value = "商品促销活动Id(活动条件id)", required = false, example = "11252")
    private Integer goodsPromotionId;
    @ApiModelProperty(value = "订单促销活动Id(活动条件id)", required = false, example = "11252")
    private Integer orderPromotionId;
    @ApiModelProperty(value = "拼团秒杀活动id", required = false, example = "11252")
    private Integer groupSeckillId;
    @ApiModelProperty(value = "货品购买数量", required = true, example = "123456")
    @NotNull(message = "P_GOODS_COUNT_NULL")
    @Min(value = 1L, message = "ORDER_ITEM_COUNT_MUST_GREATER_THAN_ZERO")
    private Integer itemCount;
    @ApiModelProperty(value = "货品在库数量", required = true, example = "10")
    @NotNull(message = "P_GOODS_IN_COUNT_NULL")
    private Integer itemInCount;
    @ApiModelProperty(value = "货品在途数量", required = true, example = "10")
    @NotNull(message = "P_GOODS_ON_WAY_COUNT_NULL")
    private Integer itemOnWayCount;
    @ApiModelProperty(value = "货品预售数量", required = true, example = "10")
    @NotNull(message = "P_GOODS_MTO_COUNT_NULL")
    private Integer itemMtoCount;
    @ApiModelProperty(value = "货品单价（如果有值，已此值为货品销售单价，没值，需通过后台计算获取货品销售单价）", required = false, example = "10.00")
    private BigDecimal salePrice;

    @Valid
    @ApiModelProperty(value = "商品定制信息(商品类型为2时必填)", required = false)
    private OrderGoodsDiyCmd diy;

}