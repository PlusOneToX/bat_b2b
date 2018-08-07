package com.bat.system.api.logistics.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/27 20:17
 */
@Data
@ApiModel(value = "LogisticsItemsInfoQry", description = "配送费用计算")
public class LogisticsItemsInfoQry {

    @NotNull(message = "P_ORDER_GOODS_ITEM_ID_NULL")
    @ApiModelProperty(value = "货品id", required = true, example = "1")
    private Integer itemId;
    @ApiModelProperty(value = "商品id")
    private Integer goodsId;
    @NotNull(message = "P_ORDER_GOODS_ITEM_COUNT_ID_NULL")
    @ApiModelProperty(value = "货品数量", required = true, example = "1")
    private Integer itemCount;
    @ApiModelProperty(value = "货品类型 1:普通商品 2：赠品 3：定制商品")
    private Integer itemType;
    @ApiModelProperty(value = "活动规则Id")
    private Integer ruleId;
    @ApiModelProperty(value = "商品等级折扣Id")
    private Integer gradeDiscountId;
}
