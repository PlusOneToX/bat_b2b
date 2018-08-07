package com.bat.order.api.order.dto.common;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/5/19 17:30
 */
@Data
@ApiModel(description = "促销活动规则信息")
public class PromotionRuleDTO implements Serializable {
    @ApiModelProperty(value = "促销活动规则id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动id", example = "123445")
    private Integer promotionId;
    @ApiModelProperty(value = "促销活动规则标签名称", example = "促销活动规则标签名称")
    private String ruleName;
    @ApiModelProperty(value = "促销活动规则标签英文名称", example = "促销活动规则标签英文名称")
    private String ruleNameEn;
    @ApiModelProperty(value = "规则对象，1订单，2商品，3货品", example = "1")
    private Short ruleType;
    @ApiModelProperty(value = "是否累计，1是 0否( 规则对象是2或3时有效)", example = "1")
    private Short addUpFlag;
    @ApiModelProperty(value = "规则形式：1金额  2数量", example = "1")
    private Short moneyOrCount;
    @ApiModelProperty(value = "规则条件列表")
    private List<PromotionRuleConditionDTO> conditions;
}
