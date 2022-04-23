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
import java.util.Date;

/**
 * 收益明细实体类
 *
 * @author lw
 * @since 2020-05-28 17:22:22
 */
@Data
public class IncomeDetailsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 订单类型
     */
    private Integer type;
    /**
     * 结算收益
     */
    private BigDecimal income;
    /**
     * 结算时间
     */
    private Date incomeTime;
    /**
     * 提现类型
     */
    private Integer WithdrawType;
    /**
     * 审核状态 0：审核中 1：审核通过 2：审核不通过
     */
    private Integer auditStatus;
    /**
     * 审核说明
     */
    private String auditDesc;
    /**
     * 订单状态
     */
    private Integer orderStatus;
}
