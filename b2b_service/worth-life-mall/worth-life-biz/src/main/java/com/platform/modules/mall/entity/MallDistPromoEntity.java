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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 推广追踪表实体
 *
 * @author 林伟
 * @since 2020-06-09 11:59:32
 */
@Data
@TableName("MALL_DIST_PROMO")
public class MallDistPromoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private String id;
    /**
     * 推广状态 1：加入购物车 2：已生成订单 3：已支付
     */
    private Integer status;
    /**
     * 购物车ID
     */
    private String cartId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 推广者用户ID
     */
    private String userId;
    /**
     * SKU_ID
     */
    private String skuId;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
}
