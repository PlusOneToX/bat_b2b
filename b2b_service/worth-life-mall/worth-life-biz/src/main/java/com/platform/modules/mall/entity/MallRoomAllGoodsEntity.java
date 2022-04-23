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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 直播接口所有商品信息实体
 *
 * @author 李鹏军
 * @since 2020-07-06 16:35:39
 */
@Data
@TableName("MALL_ROOM_ALL_GOODS")
public class MallRoomAllGoodsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品 id
     */
    @TableId(type = IdType.INPUT)
    private Integer goodsId;
    /**
     * 商品图片
     */
    private String coverImgUrl;
    /**
     * 商品名
     */
    private String name;
    /**
     * 1:一口价，此时读price字段; 2:价格区间，此时price字段为左边界，price2字段为右边界; 3:折扣价，此时price字段为原价，price2字段为现价；
     */
    private Integer priceType;
    /**
     * 商品价格
     */
    private String price;
    /**
     * 商品价格
     */
    private String price2;
    /**
     * 商品url
     */
    private String url;
    /**
     * 0：未审核，1：审核中，2:审核通过，3审核失败
     */
    private Integer auditStatus;
    /**
     * 1, 2：表示是为api添加商品，否则是在MP添加商品
     */
    private Integer thirdPartyTag;
    /**
     * 审核单ID
     */
    private Integer auditId;
}
