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
 * @since 2021-01-07 11:20:34
 */
@Data
@TableName("MALL_DISTRIBUTION_RECORD")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallDistributionRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;
    /**
     * 邀请用户id
     */
    @ApiModelProperty("邀请用户id")
    private String subordinateId;
    @ApiModelProperty("下单用户id")
    private String originId;

    /**
     * 等级
     */
    @ApiModelProperty("等级")
    private Integer level;
    /**
     * 下单总金额
     */
    @ApiModelProperty("下单总金额")
    private BigDecimal orderActualPrice;
    /**
     * 业务id
     */
    @ApiModelProperty("订单id")
    @Excel(name = "订单编号", width = 15)
    private String pid;
    /**
     * 奖励
     */
    @ApiModelProperty("奖励")
    @Excel(name = "奖励金额", width = 15)
    private BigDecimal award;
    /**
     * 标注
     */
    @ApiModelProperty("标注")
    private String remark;
    /**
     * 积分
     */
    @ApiModelProperty("积分")
    private BigDecimal integral;
    /**
     * 关系深度
     */
    @ApiModelProperty("关系深度")
    private Integer relation;


    @ApiModelProperty("比例")
    @Excel(name = "奖励比例", width = 15)
    private BigDecimal ratio;
    @Excel(name = "订单实付金额", width = 15)
    @ApiModelProperty("订单金额")
    private BigDecimal actualPrice;

    @ApiModelProperty("下单用户昵称")
    @TableField(exist = false)
    private String originNikeName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("折扣")
    @TableField(exist = false)
    private String discount;
    @ApiModelProperty("店铺名称")
    @TableField(exist = false)
    private String shopName;

}
