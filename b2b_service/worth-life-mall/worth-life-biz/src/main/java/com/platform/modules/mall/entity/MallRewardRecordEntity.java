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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合伙人奖励记录表实体
 *
 * @author pfrong
 * @since 2020-11-10 16:14:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("MALL_REWARD_RECORD")
public class MallRewardRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 用户id(该笔奖励所属的用户)
     */
    private String userId;

    @TableField(exist = false)
    private String userName;

    /**
     * 产生订单用户id
     */
    private String orderUserId;
    /**
     * 产生订单用户名
     */
    @TableField(exist = false)
    private String orderUserName;

    /**
     * 下单用户名称
     */
    @TableField(exist = false)
    private String orderUserNickName;
    /**
     * 订单实际支付金额
     */
    private BigDecimal actualPrice;
    /**
     * 奖励金额
     */
    private BigDecimal rewardAmount;
    /**
     * 奖励比例
     */
    private BigDecimal rewardRate;
    /**
     * 奖励时间
     */
    private Date createTime;
}
