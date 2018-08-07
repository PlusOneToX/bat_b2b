package com.bat.order.api.cart.dto.data;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/5/19 17:30
 */
@Data
@ApiModel(description = "购物车商品促销活动规则条件赠品信息")
public class PromotionRuleConditionPresentDTO implements Serializable {
    @ApiModelProperty(value = "促销活动规则条件赠品id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "促销活动规则条件id", example = "123445")
    private Integer promotionRuleConditionId;
    @ApiModelProperty(value = "商品id", example = "123445")
    private Integer goodsId;
    @ApiModelProperty(value = "商品编码", example = "商品名称")
    private String goodsNo;
    @ApiModelProperty(value = "商品名称", example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品英文名称", example = "商品名称")
    private String goodsNameEn;
    @ApiModelProperty(value = "货品id(规则对象为货品时有值)", example = "123445")
    private Integer itemId;
    @ApiModelProperty(value = "货品编码(规则对象为货品时有值)", example = "货品编码")
    private String itemCode;
    @ApiModelProperty(value = "货品名称(规则对象为货品时有值)", example = "货品名称")
    private String itemName;
    @ApiModelProperty(value = "货品英文名称(规则对象为货品时有值)", example = "货品名称")
    private String itemNameEn;
    @ApiModelProperty(value = "单次赠送数量(为空时不限制)", example = "10")
    private Integer count;
    @ApiModelProperty(value = "赠送总数量(为空时不限制)", example = "10")
    private Integer totalCount;
    @ApiModelProperty(value = "货品颜色值名称", example = "货品颜色值名称")
    private String colorName;
    @ApiModelProperty(value = "货品规格值名称", example = "货品规格值名称")
    private String specsName;
    @ApiModelProperty(value = "货品颜色值英文名称", example = "货品颜色值英文名称")
    private String colorNameEn;
    @ApiModelProperty(value = "货品规格值英文名称", example = "货品规格值英文名称")
    private String specsNameEn;
}
