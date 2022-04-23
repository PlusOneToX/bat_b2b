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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分销实体
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
@Data
@TableName("MALL_DIST")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallDistEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 会员ID
     */
    private String userId;
    /**
     * 上级分销ID
     */
    private Integer superiorId;
    /**
     * 上级分销ID
     */
    private String name;
    /**
     * 是否已审核
     */
    private Boolean isAudit;
    /**
     * 加入时间
     */
    private Date joinTime;
    /**
     * 待取佣金
     */
    private BigDecimal amountAvailable;
    /**
     * 已取佣金
     */
    private BigDecimal amountWithdrawn;
    /**
     * 累计佣金
     */
    private BigDecimal amountTotal;
    /**
     * 邀请码
     */
    private String invitationCode;
    /**
     * 会员昵称
     */
    @TableField(exist = false)
    private String nickname;
    /**
     * 上级分销
     */
    @TableField(exist = false)
    private String superiorNickname;
    /**
     * 会员头像
     */
    @TableField(exist = false)
    private String headImgUrl;
}
