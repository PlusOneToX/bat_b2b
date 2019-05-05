package com.bat.promotion.api.promotion.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "促销活动规则商品货品信息")
public class PromotionRuleGoodsDTO {
    @ApiModelProperty(value = "促销活动规则id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动id", example = "123445")
    private Integer promotionId;
    @ApiModelProperty(value = "促销活动规则id", example = "123445")
    private Integer promotionRuleId;
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
}
