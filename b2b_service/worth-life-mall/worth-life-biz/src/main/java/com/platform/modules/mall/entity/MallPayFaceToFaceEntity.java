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
 * 当面付记录实体
 *
 * @author 李鹏军
 * @since 2020-08-12 16:31:15
 */
@Data
@TableName("MALL_PAY_FACE_TO_FACE")
public class MallPayFaceToFaceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 会员ID
     */
    private String userId;
    /**
     * 用户下单来源类型 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序
     */
    private Integer fromType;
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 付款状态 1:未付款 2:付款中 3:已付款
     */
    private Integer payStatus;
    /**
     * 实际支付的金额
     */
    private BigDecimal actualPrice;
    /**
     * 下单时间
     */
    private Date addTime;
    /**
     * 付款时间
     */
    private Date payTime;
    /**
     * 店铺ID
     */
    private String shopsId;
    /**
     * 店铺名称
     */
    @TableField(exist = false)
    private String shopsName;
    /**
     * 会员昵称
     */
    @TableField(exist = false)
    private String nickname;
}
