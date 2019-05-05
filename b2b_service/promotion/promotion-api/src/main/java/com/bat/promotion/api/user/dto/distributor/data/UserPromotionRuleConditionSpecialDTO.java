package com.bat.promotion.api.user.dto.distributor.data;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "促销活动规则条件特价商品信息")
public class UserPromotionRuleConditionSpecialDTO {
    @ApiModelProperty(value = "促销活动规则条件特价商品id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动规则条件id", example = "123445")
    private Integer promotionRuleConditionId;
    @ApiModelProperty(value = "商品id", example = "123445")
    private Integer goodsId;
    @ApiModelProperty(value = "货品id", example = "123445")
    private Integer itemId;
    @ApiModelProperty(value = "活动特价", example = "10.00")
    private BigDecimal specialPrice;
}
