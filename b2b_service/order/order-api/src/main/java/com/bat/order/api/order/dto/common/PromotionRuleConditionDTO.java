package com.bat.order.api.order.dto.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/5/19 17:30
 */
@Data
@ApiModel(description = "促销活动规则条件信息")
public class PromotionRuleConditionDTO implements Serializable {
    @ApiModelProperty(value = "促销活动规则条件id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动id", example = "123445")
    private Integer promotionId;
    @ApiModelProperty(value = "促销活动规则id", example = "123445")
    private Integer promotionRuleId;
    @ApiModelProperty(value = "促销活动规则条件标签名称", example = "促销活动规则条件标签名称")
    private String conditionName;
    @ApiModelProperty(value = "促销活动规则条件标签英文名称", example = "促销活动规则条件标签英文名称")
    private String conditionNameEn;
    @ApiModelProperty(value = "是否特价，1是 0否 ", example = "2")
    private Short specialFlag;
    @ApiModelProperty(value = "一次性购满数量", example = "100")
    private Integer oneBuyCount;
    @ApiModelProperty(value = "一次性购满金额", example = "100.00")
    private BigDecimal oneBuyMoney;
    @ApiModelProperty(value = "促销统计方式：1满减  2满赠", example = "1")
    private Short reduceOrPresent;
    @ApiModelProperty(value = "满减类型，1减免  2折扣 ", example = "1")
    private Short reduceType;
    @ApiModelProperty(value = "满减满赠是否叠加，1是  0否", example = "1")
    private Short reductionPresentAddFlag;
    @ApiModelProperty(value = "折扣", example = "100.00")
    private BigDecimal discount;
    @ApiModelProperty(value = "减免", example = "100.00")
    private BigDecimal reduction;
    @ApiModelProperty(value = "满赠数量", example = "100")
    private Integer presentCount;

    @ApiModelProperty(value = "赠品列表")
    private List<PromotionRuleConditionPresentDTO> presents;
    @ApiModelProperty(value = "货品特价(是否特价为是时有值)")
    private BigDecimal specialPrice;
}
