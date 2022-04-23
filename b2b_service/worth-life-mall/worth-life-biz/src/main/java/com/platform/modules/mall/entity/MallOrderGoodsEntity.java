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
import java.math.BigDecimal;

/**
 * 订单详情实体
 *
 * @author 李鹏军
 * @since 2019-07-05 19:29:18
 */
@Data
@TableName("MALL_ORDER_GOODS")
public class MallOrderGoodsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品编码
     */
    private String goodsSn;
    /**
     * SKU_ID
     */
    private String skuId;
    /**
     * 商品数量
     */
    private Integer number;
    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    /**
     * 零售价格
     */
    private BigDecimal retailPrice;
    /**
     * 商品规格详情
     */
    private String goodsSpecifitionNameValue;
    /**
     * 图片链接
     */
    private String listPicUrl;
//    private Integer isGain;
}
