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
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 邀请记录表实体
 *
 * @author pfrong
 * @since 2020-11-10 14:35:24
 */
@Data
@TableName("MALL_USER_INVITE_RECORD")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallUserInviteRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;
    /**
     * 邀请人用户id
     */

    @ApiModelProperty("邀请人用户id")
    private String inviteUserId;

    /**
     * 邀请时间
     */
    private Date createTime;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String userName;

    /**
     * 邀请人用户姓名
     */
    @ApiModelProperty("邀请人用户姓名")
    private String inviteUserName;
    @TableField(exist = false)
    @ApiModelProperty("关系 1直接 2以上间接")
    private Integer relation;
    @TableField(exist = false)
    @ApiModelProperty("等级名称")
    private String levelName;

    @TableField(exist = false)
    @ApiModelProperty("用户账号")
    private String userAccountNumber;


    @TableField(exist = false)
    @ApiModelProperty("直推数量")
    private Integer directCount;

    @TableField(exist = false)
    @ApiModelProperty("间推数量")
    private Integer indirectCount;

    @TableField(exist = false)
    @ApiModelProperty("购买总金额")
    private BigDecimal orderActualPrice;

    @TableField(exist = false)
    @ApiModelProperty("返佣金额")
    private BigDecimal ratioSum;

    @TableField(exist = false)
    @ApiModelProperty("加入时间")
    private Date joinTime;

    @TableField(exist = false)
    @ApiModelProperty("店铺数量")
    private Integer shopCount;

    @ApiModelProperty("店铺用户下单总金额")
    @TableField(exist = false)
    private BigDecimal actualPrice;

    @ApiModelProperty("返佣总金额")
    @TableField(exist = false)
    private BigDecimal award;


    @ApiModelProperty("店铺用户下单总金额")
    @TableField(exist = false)
    private BigDecimal directPushPrice;

    @ApiModelProperty("店铺返佣总金额")
    @TableField(exist = false)
    private BigDecimal directCommission;

    @ApiModelProperty("推广者返佣总金额")
    @TableField(exist = false)
    private BigDecimal indirectCommission;

}
