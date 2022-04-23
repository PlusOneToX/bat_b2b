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

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 实体
 *
 * @author 李鹏军
 * @since 2021-01-05 17:36:02
 */
@Data
@TableName("MALL_USER_BONUS_DETAIL")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallUserBonusDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @Excel(name = "用户id", width = 15)
    private String userId;


    /**
     * 积分
     */
    @Excel(name = "积分", width = 15)
    @ApiModelProperty("积分")
    private BigDecimal integral;
    /**
     * 奖励
     */
    @ApiModelProperty("奖励金额")
    @Excel(name = "奖励金额", width = 15)
    private BigDecimal award;
    @ApiModelProperty("比例")
    @Excel(name = "比例", width = 15)
    private BigDecimal ratio;
    /**
     * 瓜分人数
     */
    @ApiModelProperty("瓜分人数")
    @Excel(name = "瓜分人数", width = 15)
    private Integer count;
    /**
     * 
     */
    @Excel(name = "时间", width = 15)
    private Date createTime;
    /**
     * 活动id
     */
    private Integer bonusPoolId;
    /**
     * 活动详情id
     */
    private Integer mallBonusPoolDetailId;
    @ApiModelProperty("排序")
    @Excel(name = "排序", width = 15)
    private Integer sort;

    @TableField(exist = false)
    @ApiModelProperty("昵称")
    @Excel(name = "昵称", width = 15)
    private String nikeName;
    @TableField(exist = false)
    @Excel(name = "备注", width = 15)
    @ApiModelProperty("备注")
    private String remark;
    @TableField(exist = false)
    @Excel(name = "头像", width = 15)
    @ApiModelProperty("头像")
    private String head;
}
