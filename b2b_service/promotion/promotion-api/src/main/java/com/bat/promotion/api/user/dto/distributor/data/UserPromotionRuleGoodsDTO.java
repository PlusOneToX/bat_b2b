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
@ApiModel(description = "促销活动规则商品货品信息")
public class UserPromotionRuleGoodsDTO {
    @ApiModelProperty(value = "货品id", example = "123456")
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
    @ApiModelProperty(value = "商品类型 1-普通 2-定制", example = "1")
    private Short goodsType;
    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制", example = "1")
    private Short diyType;
    @ApiModelProperty(value = "商品图片地址1", example = "商品名称")
    private String imageUrl1;
    @ApiModelProperty(value = "货品编码(规则对象为货品时有值)", example = "货品编码")
    private String itemCode;
    @ApiModelProperty(value = "货品条形码", example = "货品条形码")
    private String barCode;
    @ApiModelProperty(value = "货品名称(规则对象为货品时有值)", example = "货品名称")
    private String itemName;
    @ApiModelProperty(value = "货品图片", example = "商品名称")
    private String itemImg;
    @ApiModelProperty(value = "货品颜色值id", example = "货品颜色值id")
    private Integer colorId;
    @ApiModelProperty(value = "货品规格值id", example = "货品规格值id")
    private Integer specsId;
    @ApiModelProperty(value = "货品颜色值名称", example = "货品颜色值名称")
    private String colorName;
    @ApiModelProperty(value = "货品规格值名称", example = "货品规格值名称")
    private String specsName;
    @ApiModelProperty(value = "重量", example = "10.9")
    private BigDecimal weight;
    @ApiModelProperty(value = "长", example = "10.9")
    private BigDecimal length;
    @ApiModelProperty(value = "宽", example = "10.9")
    private BigDecimal width;
    @ApiModelProperty(value = "高", example = "10.9")
    private BigDecimal height;
}
