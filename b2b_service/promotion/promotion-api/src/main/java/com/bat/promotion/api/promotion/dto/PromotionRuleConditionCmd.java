package com.bat.promotion.api.promotion.dto;

import java.math.BigDecimal;
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
@ApiModel(description = "促销活动规则条件信息")
public class PromotionRuleConditionCmd {
    @ApiModelProperty(value = "促销活动规则条件id(修改情况必填)", required = false, example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动id", required = false, example = "123445")
    private Integer promotionId;
    @ApiModelProperty(value = "促销活动规则id", required = false, example = "123445")
    private Integer promotionRuleId;
    @NotBlank(message = "P_PROMOTION_RULE_CONDITION_NAME_NULL")
    @ApiModelProperty(value = "促销活动规则条件标签名称", required = true, example = "促销活动规则条件标签名称")
    private String conditionName;
    @ApiModelProperty(value = "促销活动规则条件标签英文名称", required = false, example = "促销活动规则条件标签英文名称")
    private String conditionNameEn;
    @NotNull(message = "P_PROMOTION_SPECIAL_FLAG_NULL")
    @ApiModelProperty(value = "是否特价，1是 0否 ", required = true, example = "2")
    private Short specialFlag;
    @ApiModelProperty(value = "一次性购满数量(规则形式为数量时必填)", required = false, example = "100")
    private Integer oneBuyCount;
    @ApiModelProperty(value = "一次性购满金额(规则形式为金额时必填)", required = false, example = "100.00")
    private BigDecimal oneBuyMoney;
    @ApiModelProperty(value = "促销统计方式：1满减  2满赠", required = false, example = "1")
    private Short reduceOrPresent;
    @ApiModelProperty(value = "满减类型，1减免  2折扣，(促销统计方式是1时必填)", required = false, example = "1")
    private Short reduceType;
    @ApiModelProperty(value = "满减满赠是否叠加，1是  0否，(满减类型为1或促销统计方式为2时必填)", required = false, example = "1")
    private Short reductionPresentAddFlag;
    @ApiModelProperty(value = "折扣(满减类型为2时必填)", required = false, example = "100.00")
    private BigDecimal discount;
    @ApiModelProperty(value = "减免(满减类型为1时必填)", required = false, example = "100.00")
    private BigDecimal reduction;
    @ApiModelProperty(value = "满赠数量(促销统计方式是2时必填)", required = false, example = "100")
    private Integer presentCount;
    @Valid
    @ApiModelProperty(value = "赠品列表(促销统计方式是2时必填)", required = false)
    private List<PromotionRuleConditionPresentCmd> presents;
    @Valid
    @ApiModelProperty(value = "特价商品列表(是否特价为1是必填)", required = false)
    private List<PromotionRuleConditionSpecialCmd> specials;

    @NotNull(message = "P_PROMOTION_OPERATION_TYPE")
    @ApiModelProperty(value = "操作类型,1.新增 2.修改 3.删除", required = true, example = "1")
    private Short operationType;
}
