package com.bat.promotion.api.promotion.dto.data;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "促销活动规则信息")
public class PromotionRuleDTO {
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
    @ApiModelProperty(value = "规则商品货品列表(规则对象为货品或商品时有值)")
    private List<PromotionRuleGoodsDTO> goods;

}
