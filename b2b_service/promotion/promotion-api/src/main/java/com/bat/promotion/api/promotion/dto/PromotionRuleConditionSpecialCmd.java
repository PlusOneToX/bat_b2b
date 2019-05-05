package com.bat.promotion.api.promotion.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "促销活动规则条件特价商品信息")
public class PromotionRuleConditionSpecialCmd implements Serializable {
    @ApiModelProperty(value = "促销活动规则条件特价商品id(修改情况必填)", required = false, example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动id", required = false, example = "123445")
    private Integer promotionId;
    @ApiModelProperty(value = "促销活动规则条件id", required = false, example = "123445")
    private Integer promotionRuleConditionId;
    @NotNull(message = "P_PROMOTION_SPECIAL_GOODS_ID_NULL")
    @ApiModelProperty(value = "商品id", example = "123445")
    private Integer goodsId;
    @NotNull(message = "P_PROMOTION_SPECIAL_GOODS_ITEM_ID_NULL")
    @ApiModelProperty(value = "货品id(规则对象为货品时有值)", example = "123445")
    private Integer itemId;
    @NotNull(message = "P_PROMOTION_SPECIAL_PRICE_NULL")
    @ApiModelProperty(value = "活动特价", example = "10.00")
    private BigDecimal specialPrice;
}
