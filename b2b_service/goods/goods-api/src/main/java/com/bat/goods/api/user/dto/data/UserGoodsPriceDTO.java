package com.bat.goods.api.user.dto.data;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/30 16:42
 */
@Data
@ApiModel(description = "商品(SPU)价格信息")
public class UserGoodsPriceDTO {
    @ApiModelProperty(value = "商品id", example = "100.00")
    private Integer goodsId;
    @ApiModelProperty(value = "活动类型，0 没活动 1促销活动 2拼团 3秒杀 ", example = "1")
    private Short promotionType;
    @ApiModelProperty(value = "是否有赠品：1有赠品  0没赠品 ", example = "1")
    private Short presentFlag;
    @ApiModelProperty(value = "商品最小价格(只有登录的用户有返回值，但也不是一定有返回值，具体情况具体分析)", example = "100.00")
    private BigDecimal minPrice;
    @ApiModelProperty(value = "商品最大价格(只有登录的用户有返回值，但也不是一定有返回值，具体情况具体分析)", example = "100.00")
    private BigDecimal maxPrice;
    @ApiModelProperty(value = "商品最小活动价格(有活动时有返回值)", example = "100.00")
    private BigDecimal promotionMinPrice;
    @ApiModelProperty(value = "商品最大活动价格(有活动时有返回值)", example = "100.00")
    private BigDecimal promotionMaxPrice;
}
