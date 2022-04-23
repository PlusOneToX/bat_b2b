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
@TableName("MALL_DIST_ORDER")
public class MallDistOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
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
     * 订单类型
     */
    private Integer type;
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
     * 审核状态 0：审核中 1：审核通过 2：审核不通过
     */
    private Integer auditStatus;
    /**
     * 审核说明
     */
    private String auditDesc;
    /**
     * 会员昵称
     */
    @TableField(exist = false)
    private String nickname;
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
     * 提现类型
     */
    private Integer withdrawType;
    /**
     * 收款方银行卡号
     */
    private String encBankNo;
    /**
     * 收款方用户名
     */
    private String encTrueName;
    /**
     * 收款方开户行
     */
    private String bankCode;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * SKU_ID
     */
    private String skuId;
    /**
     * 企业付款成功，返回的微信付款单号
     */
    private String paymentNo;
}
