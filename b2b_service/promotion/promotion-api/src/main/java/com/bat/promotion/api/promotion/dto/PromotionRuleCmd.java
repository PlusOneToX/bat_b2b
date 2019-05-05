package com.bat.promotion.api.promotion.dto;

import java.util.List;

import javax.validation.Valid;
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
@ApiModel(description = "促销活动规则信息")
public class PromotionRuleCmd {
    @ApiModelProperty(value = "促销活动规则id(修改情况必填)", required = false, example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动id", required = false, example = "123445")
    private Integer promotionId;
    @NotBlank(message = "P_PROMOTION_RULE_NAME_NULL")
    @ApiModelProperty(value = "促销活动规则标签名称", required = true, example = "促销活动规则标签名称")
    private String ruleName;
    @ApiModelProperty(value = "促销活动规则标签英文名称", required = false, example = "促销活动规则标签英文名称")
    private String ruleNameEn;
    @NotNull(message = "P_PROMOTION_RULE_TYPE_NULL")
    @ApiModelProperty(value = "规则对象，1订单，2商品，3货品", required = true, example = "1")
    private Short ruleType;
    @NotNull(message = "P_PROMOTION_ADD_UP_FLAG_NULL")
    @ApiModelProperty(value = "是否累计，1是 0否( 规则对象是2或3时有效)", required = true, example = "1")
    private Short addUpFlag;
    @NotNull(message = "P_PROMOTION_MONEY_OR_COUNT_NULL")
    @ApiModelProperty(value = "规则形式：1金额  2数量", required = true, example = "1")
    private Short moneyOrCount;
    @Valid
    @NotNull(message = "P_PROMOTION_RULE_CONDITION_NULL")
    @ApiModelProperty(value = "规则条件列表", required = true)
    private List<PromotionRuleConditionCmd> conditions;
    @Valid
    @ApiModelProperty(value = "规则商品货品列表(规则对象为货品或商品时有值)", required = false)
    private List<PromotionRuleGoodsCmd> goods;

}
