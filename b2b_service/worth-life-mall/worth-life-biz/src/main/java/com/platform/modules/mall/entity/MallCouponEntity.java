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
import java.util.List;

/**
 * 优惠券列表实体
 *
 * @author 李鹏军
 * @since 2020-04-10 13:52:21
 */
@Data
@TableName("MALL_COUPON")
public class MallCouponEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 优惠券标题
     */
    private String title;
    /**
     * 优惠券编号
     */
    private String couponSn;
    /**
     * 优惠券类型：1:代金券 2:折扣
     */
    private Integer couponType;
    /**
     * 最低消费金额
     */
    private BigDecimal minPrice;
    /**
     * 优惠金额
     */
    private BigDecimal subPrice;
    /**
     * 折扣率
     */
    private BigDecimal discount;
    /**
     * 有效期
     */
    private Date beginTime;
    /**
     * 有效期
     */
    private Date endTime;
    /**
     * 可领取时间
     */
    private Date beginGetTime;
    /**
     * 可领取时间
     */
    private Date endGetTime;
    /**
     * 优惠券数量
     */
    private Integer totalCount;
    /**
     * 已发放数量
     */
    private Integer sendCount;
    /**
     * 指定使用类型 0：全场通用券 1：指定商品 2：指定品牌
     */
    private Integer limitType;
    /**
     * 每人限领数量
     */
    private Integer limitUser;
    /**
     * 状态 1:正常 2：过期 3：禁用
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 指定的商品ID
     */
    @TableField(exist = false)
    private List<String> goodsIds;
    /**
     * 指定的品牌ID
     */
    @TableField(exist = false)
    private List<String> brandIds;
}
