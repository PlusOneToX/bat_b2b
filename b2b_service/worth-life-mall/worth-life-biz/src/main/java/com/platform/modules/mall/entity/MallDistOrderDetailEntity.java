/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.modules.mall.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分销订单实体
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
@Data
public class MallDistOrderDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 购买者ID
     */
    private String buyerId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 结算收益
     */
    private BigDecimal income;
    /**
     * 结算时间
     */
    private Date incomeTime;
    /**
     * 提成级数
     */
    private Integer commissionType;
    /**
     * 提成比例
     */
    private BigDecimal commission;
    /**
     * 购买者昵称
     */
    @TableField(exist = false)
    private String buyerNickname;
    /**
     * 订单编号
     */
    @TableField(exist = false)
    private String orderSn;
    /**
     * 实际支付的金额
     */
    private BigDecimal actualPrice;
    /**
     * 订单状态 -1:秒杀成功(这里只是秒杀成功，还需要用户下单补充订单信息) 0:订单创建成功等待付款 100:订单超时已过期 101:订单已取消 102:订单已删除 201:订单已付款,等待发货 300:订单已发货 301:用户确认收货 401:没发货，退款　402:已收货，退款退货
     */
    private Integer orderStatus;
    /**
     * 商品主键
     */
    private String goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 列表图
     */
    private String goodsPic;
}
