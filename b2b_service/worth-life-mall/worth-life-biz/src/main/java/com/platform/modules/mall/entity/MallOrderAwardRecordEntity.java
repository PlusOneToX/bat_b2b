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
 * @since 2021-01-08 17:26:14
 */
@Data
@TableName("MALL_ORDER_AWARD_RECORD")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallOrderAwardRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 订单id
     */
    private String pid;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 下单用户id
     */
    private String originUserId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 关系
     */
    private Integer relation;
    /**
     * 奖励金额
     */
    private BigDecimal amount;
    /**
     * 订单金额
     */
    private BigDecimal orderPrice;

    /**
     *  折扣
     */
    @TableField(exist = false)
    private String discount;

}
