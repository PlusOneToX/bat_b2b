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
 * 首页轮播配置实体
 *
 * @author 李鹏军
 * @since 2019-07-01 10:57:03
 */
@Data
@TableName("MALL_BANNER")
public class MallBannerEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 类型 0:图片 1:链接 2:文本 3:视频
     */
    private Integer mediaType;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片
     */
    private String imageUrl;
    /**
     * 链接
     */
    private String link;
    /**
     * 视频链接
     */
    private String videoUrl;
    /**
     * 文本
     */
    private String content;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 状态 0:禁用 1:启用
     */
    private Integer enabled;
}
