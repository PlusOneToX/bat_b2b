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
@ApiModel(description = "订单促销活动信息")
public class OrderPromotionDTO implements Serializable {
    @ApiModelProperty(value = "促销活动列表")
    private List<PromotionDTO> promotions;
    @ApiModelProperty(value = "拼团秒杀活动")
    private List<GroupSeckillDTO> spellGroups;
}
