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
 * 搜索关键词实体
 *
 * @author 李鹏军
 * @since 2019-07-03 12:02:04
 */
@Data
@TableName("MALL_KEYWORDS")
public class MallKeywordsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 场景 0:小程序 1:公众号 2:APP 3:PC
     */
    private Integer type;
    /**
     * 热搜 0:否 1:是
     */
    private Integer isHot;
    /**
     * 默认 0:否 1:是
     */
    private Integer isDefault;
    /**
     * 显示 0:否 1:是
     */
    private Integer isShow;
    /**
     * 关键词的跳转链接
     */
    private String schemeUrl;
    /**
     * 排序
     */
    private Integer sort;
}
