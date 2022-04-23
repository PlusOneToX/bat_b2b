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

/**
 * 实体
 *
 * @author 李鹏军
 * @since 2020-03-08 10:25:25
 */
@Data
@TableName("MALL_TOPIC")
public class MallTopicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 活动主题
     */
    private String title;
    /**
     * 活动内容
     */
    private String content;
    /**
     * 化名
     */
    private String avatar;
    /**
     * 活动条例图片
     */
    private String itemPicUrl;
    /**
     * 子标题
     */
    private String subtitle;
    /**
     * 活动类别
     */
    private Integer topicCategoryId;
    /**
     * 活动类别主题
     */
    @TableField(exist = false)
    private String categoryTitle;
    /**
     * 活动价格
     */
    private BigDecimal priceInfo;
    /**
     *
     */
    private String readCount;
    /**
     * 场景图片链接
     */
    private String scenePicUrl;
    /**
     * 活动模板Id
     */
    private Integer topicTemplateId;
    /**
     * 活动标签Id
     */
    private Integer topicTagId;
}
