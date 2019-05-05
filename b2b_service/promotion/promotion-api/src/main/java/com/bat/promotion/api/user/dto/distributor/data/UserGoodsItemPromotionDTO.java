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
@ApiModel(description = "货品(SKU)活动信息")
public class UserGoodsItemPromotionDTO {
    @ApiModelProperty(value = "货品id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "货品(SKU)促销活动列表")
    private List<UserPromotionDTO> promotions;
    @ApiModelProperty(value = "货品(SKU)拼团秒杀活动列表")
    private List<UserGoodsItemGroupSeckillDTO> groupSeckills;
}
