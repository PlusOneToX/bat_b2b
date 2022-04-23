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
import java.util.Date;

/**
 * 会员足迹实体
 *
 * @author 李鹏军
 * @since 2019-07-02 17:25:40
 */
@Data
@TableName("MALL_FOOTPRINT")
public class MallFootprintEntity implements Serializable {
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
     * 商品ID
     */
    private String goodsId;
    /**
     * 记录时间
     */
    private Date addTime;
    /**
     * 微信昵称
     */
    @TableField(exist = false)
    private String nickname;
    /**
     * 用户头像
     */
    @TableField(exist = false)
    private String headImgUrl;
    /**
     * 商品
     */
    @TableField(exist = false)
    private String goodsName;
    @TableField(exist = false)
    private String goodsSum;
    @TableField(exist = false)
    private String listPicUrl;
    @TableField(exist = false)
    private String retailPrice;
    @TableField(exist = false)
    private String goodsBrief;
    @TableField(exist = false)
    private String sales;
    @TableField(exist = false)
    private String marketPrice;
}
