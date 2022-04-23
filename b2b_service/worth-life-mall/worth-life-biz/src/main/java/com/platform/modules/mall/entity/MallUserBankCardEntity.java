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
 * 用户银行卡表实体
 *
 * @author 李鹏军
 * @since 2020-06-15 10:14:45
 */
@Data
@TableName("MALL_USER_BANK_CARD")
public class MallUserBankCardEntity implements Serializable {
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
     * 会员昵称
     */
    @TableField(exist = false)
    private String nickname;
    /**
     * 收款人姓名
     */
    private String cardName;
    /**
     * 银行卡号
     */
    private String cardNumber;
    /**
     * 卡类型
     */
    private String cardType;
    /**
     * 银行ID
     */
    private Integer bankTypeId;
    /**
     * 银行名称
     */
    @TableField(exist = false)
    private String bankName;
    /**
     * 银行code
     */
    @TableField(exist = false)
    private String bankCode;
    /**
     * 1：已绑定 2：已解绑
     */
    private Integer cardStatus;
    /**
     * 绑定时间
     */
    private Date boundAt;
}
