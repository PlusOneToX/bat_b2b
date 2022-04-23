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

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 用户账户余额变动记录实体
 *
 * @author 李鹏军
 * @since 2019-07-11 16:56:08
 */
@Data
@TableName("MALL_ACCOUNT_LOG")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallAccountLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户下单来源类型 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序 7:分销奖励 8:合伙人奖励 10:排行奖励
     */
    private Integer fromType;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 类型：0=待支付 1=收入，2=支出
     */
    private Integer type;
    /**
     * 变动金额
     */
    private BigDecimal price;
    /**
     * 变动说明
     */
    private String logDesc;
    /**
     *
     */
    private Date addTime;
    /**
     * 订单类型 0--充值 1--商城订单 2--秒杀订单 3--拼团订单 4--商城订单退款 5--秒杀订单退款 6--拼团订单退款 7--合伙人分成
     */
    private Integer orderType;
    /**
     * 订单编号
     */
    private String orderSn;
    private String remark;
    /**
     * 微信昵称
     */
    @TableField(exist = false)
    private String nickname;
}
