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
import java.util.List;

/**
 * 商品SKU实体
 *
 * @author 李鹏军
 * @since 2019-07-04 00:05:33
 */
@Data
@TableName("MALL_GOODS_SKU")
public class MallGoodsSkuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 商品编码
     */
    private String goodsSn;
    /**
     * 商品库存
     */
    private Integer goodsNumber;
    /**
     * 零售价格
     */
    private BigDecimal retailPrice;
    /**
     * 价格
     */
    private BigDecimal marketPrice;
    /**
     * 图片
     */
    private String listPicUrl;
    /**
     * 规格值
     */
    @TableField(exist = false)
    private List<KeyValue> keyValue;
}
