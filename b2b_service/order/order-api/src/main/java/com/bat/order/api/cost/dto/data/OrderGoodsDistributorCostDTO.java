package com.bat.order.api.cost.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: 订单商品的费用信息 与商品对应 商品-》消费 一对多
 * @date: 2018/6/24 23:56
 */
@Data
public class OrderGoodsDistributorCostDTO implements Cloneable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单明细id")
    private Integer orderGoodsId;
    @ApiModelProperty("直属分销商id")
    private Integer distributorId;
    @ApiModelProperty("是否赠品 1 非赠品 2 赠品")
    private Short itemType;
    @ApiModelProperty("分销商会员单价")
    private BigDecimal salePrice;
    @ApiModelProperty("实际单价")
    private BigDecimal actualPrice;
    @ApiModelProperty("商品促销活动Id(活动条件id)")
    private Integer goodsPromotionId;
    @ApiModelProperty("订单促销活动Id(活动条件id)")
    private Integer orderPromotionId;
    @ApiModelProperty("拼团秒杀活动id")
    private Integer spellGroupId;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("分销商名称")
    private String distributorName;

    @SneakyThrows
    @Override
    public OrderGoodsDistributorCostDTO clone() {
        return (OrderGoodsDistributorCostDTO)super.clone();
    }
}
