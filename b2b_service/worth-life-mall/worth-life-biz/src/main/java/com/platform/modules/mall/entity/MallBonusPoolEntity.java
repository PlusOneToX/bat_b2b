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
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实体
 *
 * @author 李鹏军
 * @since 2021-01-05 16:03:10
 */
@Data
@TableName("MALL_BONUS_POOL")
public class MallBonusPoolEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 活动标题
     */
    @ApiModelProperty("活动标题")
    private String activityTitle;
    /**
     * 活动详情
     */
    @ApiModelProperty("活动详情")
    private String activityDetail;
    /**
     * 奖金总数
     */
    @ApiModelProperty("奖金总数")
    private BigDecimal amountTotal;
    /**
     * 0未开始 1进行中 2已结束
     */
    @ApiModelProperty("0未开始 1进行中 2已结束")
    private Integer status;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private Date endTime;
    /**
     * 是否已发放
     */
    @ApiModelProperty("是否已发放")
    private Integer isGain;
    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private List<MallBonusPoolDetailEntity> mallBonusPoolDetailEntities=new ArrayList<>();
    @TableField(exist = false)
    private List<MallUserBonusDetailEntity> mallUserBonusDetailEntities=new ArrayList<>();
}
