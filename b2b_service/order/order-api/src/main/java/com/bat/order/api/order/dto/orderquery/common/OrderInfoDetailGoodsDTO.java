package com.bat.order.api.order.dto.orderquery.common;

import java.util.List;
import java.util.stream.Collectors;

import com.bat.order.api.cost.dto.data.OrderGoodsDistributorCostDTO;

import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: 自定义查询封装返回 默认分销商商品信息 订单所有等级分销商商品信息
 * @date: 2018/6/22 12:43
 */
@Data
public class OrderInfoDetailGoodsDTO implements Cloneable {
    /**
     * 商品信息
     */
    private OrderGoodsDTO orderGoods;
    /**
     * 定制商品扩展信息
     */
    private OrderGoodsDiyDTO orderGoodsDiy;

    /**
     * 默认分销商 商品信息
     */
    private OrderGoodsDistributorCostDTO orderGoodsDistributorCost;

    /**
     * C端信息数据
     */
    private OrderGoodsCustomerCostDTO orderGoodsCustomerCost;

    /**
     * 单个商品下的所有分销商等级信息
     */
    private List<OrderGoodsDistributorCostDTO> orderGoodsDistributorCosts;

    /**
     * 兑换卡商品 有兑换卡数据
     */
    private OrderGoodsExchangeCodeDTO orderGoodsExchangeCode;

    @SneakyThrows
    @Override
    public OrderInfoDetailGoodsDTO clone() {
        OrderInfoDetailGoodsDTO clone = (OrderInfoDetailGoodsDTO)super.clone();
        this.setOrderGoods(this.orderGoods.clone());
        if (this.orderGoodsDiy != null) {
            this.setOrderGoodsDiy(this.orderGoodsDiy.clone());
        }
        this.setOrderGoodsDistributorCosts(
            orderGoodsDistributorCosts.stream().map(OrderGoodsDistributorCostDTO::clone).collect(Collectors.toList()));
        return clone;
    }
}