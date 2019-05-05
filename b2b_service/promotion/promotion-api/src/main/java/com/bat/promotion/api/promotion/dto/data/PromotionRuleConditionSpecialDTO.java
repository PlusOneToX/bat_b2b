package com.bat.promotion.api.promotion.dto.data;

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
public class PromotionRuleConditionSpecialDTO {
    @ApiModelProperty(value = "促销活动规则条件特价商品id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动规则条件id", example = "123445")
    private Integer promotionRuleConditionId;
    @ApiModelProperty(value = "商品id", example = "123445")
    private Integer goodsId;
    @ApiModelProperty(value = "商品编码", example = "商品名称")
    private String goodsNo;
    @ApiModelProperty(value = "商品名称", example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "货品id(规则对象为货品时有值)", example = "123445")
    private Integer itemId;
    @ApiModelProperty(value = "货品编码(规则对象为货品时有值)", example = "货品编码")
    private String itemCode;
    @ApiModelProperty(value = "货品名称(规则对象为货品时有值)", example = "货品名称")
    private String itemName;
    @ApiModelProperty(value = "活动特价", example = "10.00")
    private BigDecimal specialPrice;
}
