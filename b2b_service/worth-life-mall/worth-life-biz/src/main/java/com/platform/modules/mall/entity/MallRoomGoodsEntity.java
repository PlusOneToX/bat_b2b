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

/**
 * 直播房间商品信息实体
 *
 * @author 李鹏军
 * @since 2020-03-30 18:52:09
 */
@Data
@TableName("MALL_ROOM_GOODS")
public class MallRoomGoodsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 房间 id
     */
    private Integer roomid;
    /**
     * 封面图片 url
     */
    private String coverImg;
    /**
     * 商品名
     */
    private String name;
    /**
     * 商品图片url
     */
    private String url;
    /**
     * 商品价格
     */
    private String price;
    /**
     * 商品价格2
     */
    private String price2;
}
