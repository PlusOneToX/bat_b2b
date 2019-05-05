package com.bat.promotion.api.promotion.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "促销活动规则商品货品信息")
public class PromotionRuleGoodsCmd {
    @ApiModelProperty(value = "促销活动规则id(修改情况必填)", required = false, example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动id", required = false, example = "123445")
    private Integer promotionId;
    @NotNull(message = "P_PROMOTION_RULE_GOODS_ID_NULL")
    @ApiModelProperty(value = "商品id", required = true, example = "123445")
    private Integer goodsId;
    @ApiModelProperty(value = "货品id(规则对象为货品时有值)", required = false, example = "123445")
    private Integer itemId;
    @NotBlank(message = "P_PROMOTION_RULE_GOODS_NO_NULL")
    @ApiModelProperty(value = "商品编码", required = true, example = "123445")
    private String goodsNo;
    @ApiModelProperty(value = "编码(规则对象为货品时有值)", required = false, example = "123445")
    private String itemCode;
}
