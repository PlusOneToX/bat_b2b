package com.bat.order.api.order.dto.distributor;

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
@ApiModel(description = "商品活动查询")
public class OrderPromotionQry implements Serializable {
    @ApiModelProperty(value = "促销活动Ids(活动条件ids)", example = "123456")
    private List<Integer> promotionIds;
    @ApiModelProperty(value = "拼团秒杀活动ids", example = "123456")
    private List<Integer> spellGroupIds;
}
