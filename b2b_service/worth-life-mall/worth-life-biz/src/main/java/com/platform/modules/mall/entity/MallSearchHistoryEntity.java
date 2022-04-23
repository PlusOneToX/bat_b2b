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
 * 搜索历史实体
 *
 * @author 李鹏军
 * @since 2019-07-02 17:44:43
 */
@Data
@TableName("MALL_SEARCH_HISTORY")
public class MallSearchHistoryEntity implements Serializable {
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
     * 关键词
     */
    private String keyword;
    /**
     * 搜索来源 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序
     */
    private Integer searchFrom;
    /**
     * 搜索时间
     */
    private Date addTime;
    /**
     * 微信昵称
     */
    @TableField(exist = false)
    private String nickname;
}
