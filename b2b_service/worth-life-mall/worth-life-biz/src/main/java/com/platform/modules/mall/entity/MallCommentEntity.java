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
import java.util.List;

/**
 * 会员评价实体
 *
 * @author 李鹏军
 * @since 2019-07-04 14:46:38
 */
@Data
@TableName("MALL_COMMENT")
public class MallCommentEntity implements Serializable {
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
     * 订单
     */
    private String orderId;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 规格属性组成的字符串，用来显示用
     */
    private String goodsSpecifitionNameValue;
    /**
     * 评价内容，储存为BASE64编码
     */
    private String content;
    /**
     * 类型 0:评论的是商品,1:评论的是文章
     */
    private Integer type;
    /**
     * 记录时间
     */
    private Date addTime;
    /**
     * 状态 是否被管理员批准显示 0:待审核 1:审核通过
     */
    private Integer status;
    /**
     * 评价级别 1-5级 默认 5
     */
    private Integer evalLevel;
    /**
     * 配送质量
     */
    private Integer deliveryLevel;
    /**
     * 商品服务
     */
    private Integer goodsLevel;
    /**
     * 微信昵称
     */
    @TableField(exist = false)
    private String nickname;
    /**
     * 商品
     */
    @TableField(exist = false)
    private String goodsName;
    /**
     * 评论图片
     */
    @TableField(exist = false)
    private List<MallCommentPictureEntity> commentPictureEntityList;
    /**
     * 评论用户的信息
     */
    @TableField(exist = false)
    private MallUserEntity userInfo;
}
