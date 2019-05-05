package com.bat.promotion.api.user.dto.distributor.data;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "商品(SPU)活动信息")
public class UserGoodsPromotionDTO {
    @ApiModelProperty(value = "商品id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "订单促销活动列表")
    private List<UserPromotionDTO> orderPromotions;
    @ApiModelProperty(value = "商品促销活动列表")
    private List<UserPromotionDTO> goodsPromotions;
    @ApiModelProperty(value = "货品(SKU)活动列表")
    private List<UserGoodsItemPromotionDTO> goodsItemPromotions;
}
