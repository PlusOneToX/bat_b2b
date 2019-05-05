package com.bat.promotion.api.promotion.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "促销活动规则条件赠品信息")
public class PromotionRuleConditionPresentDTO {
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
    @ApiModelProperty(value = "货品id(规则对象为货品时有值)", example = "123445")
    private Integer itemId;
    @ApiModelProperty(value = "货品编码(规则对象为货品时有值)", example = "货品编码")
    private String itemCode;
    @ApiModelProperty(value = "货品名称(规则对象为货品时有值)", example = "货品名称")
    private String itemName;
    @ApiModelProperty(value = "商品图片地址", example = "商品图片地址")
    private String imageUrl1;
    @ApiModelProperty(value = "商品英文图片地址", example = "商品图片地址")
    private String imageUrl1en;
    @ApiModelProperty(value = "货品图片地址", example = "商品图片地址")
    private String itemImg;
    @ApiModelProperty(value = "单次赠送数量(为空时不限制)", example = "10")
    private Integer count;
    @ApiModelProperty(value = "赠送总数量(为空时不限制)", example = "10")
    private Integer totalCount;
}
