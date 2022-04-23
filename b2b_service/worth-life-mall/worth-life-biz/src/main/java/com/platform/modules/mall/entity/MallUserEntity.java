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
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.BigDecimalUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户
 *
 * @author 李鹏军
 */
@Data
@TableName("MALL_USER")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty("用户ID")
    private String id;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty(hidden = true)
    private String password;
    /**
     * 用户的性别（1是男性，2是女性，0是未知）
     */
    @ApiModelProperty("用户的性别（1是男性，2是女性，0是未知）")
    private Integer gender;
    /**
     * 生日
     */
    @ApiModelProperty("生日")
    private Date birthday;
    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private Date registerTime;
    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    private Date lastLoginTime;
    /**
     * 最后登录IP
     */
    @ApiModelProperty("最后登录IP")
    private String lastLoginIp;
    /**
     * 会员等级ID
     */
    @ApiModelProperty("")
    private String userLevelId;
    /**
     * 微信昵称
     */
    @ApiModelProperty("微信昵称")
    private String nickname;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;
    /**
     * 注册ip
     */
    @ApiModelProperty("注册ip")
    private String registerIp;
    /**
     * 用户头像
     */
    @ApiModelProperty(":用户头像")
    private String headImgUrl;
    /**
     * 支付宝用户标识
     */
    @ApiModelProperty("支付宝用户标识")
    private String aliUserId;
    /**
     * 微信小程序用户的标识
     */
    @ApiModelProperty("微信小程序用户的标识")
    private String openId;
    /**
     * QQ小程序用户的标识
     */
    @ApiModelProperty("QQ小程序用户的标识")
    private String qqOpenId;
    /**
     * 需要用户将公众号、小程序绑定到微信开放平台帐号
     * 同一主体下的unionId一致
     */
    @ApiModelProperty("需要用户将公众号、小程序绑定到微信开放平台帐号")
    private String unionId;
    /**
     * 公众号关注状态（1是关注，0是未关注），未关注时获取不到其余信息
     */
    @ApiModelProperty("公众号关注状态（1是关注，0是未关注），未关注时获取不到其余信息")
    private Integer subscribe;
    /**
     * 用户关注公众号时间，为时间戳。如果用户曾多次关注，则取最后关注时间
     */
    @ApiModelProperty("用户关注公众号时间，为时间戳。如果用户曾多次关注，则取最后关注时间")
    private String subscribeTime;
    /**
     * 签到、购物获得总积分
     */
    @ApiModelProperty("签到、购物获得总积分")
    private BigDecimal signAllIntegral = new BigDecimal(0);
    /**
     * 已兑换积分
     */
    @ApiModelProperty("已兑换积分")
    private BigDecimal signUsedIntegral = new BigDecimal(0);
    /**
     * 余额
     */
    @ApiModelProperty("余额")
    private BigDecimal balance = new BigDecimal(0);

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String userLevelName;

//    @TableField(exist = false)
//    @ApiModelProperty(hidden = true)
//    private Boolean isDistributor;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "邀请人id")
    private String inviteUserId;

    @ApiModelProperty(value = "1普通用户 2商家 3认证中 4推广者")
    private Integer roleType;

    @ApiModelProperty(value = "等级")
    private Integer level;

    @ApiModelProperty("我的收藏")
    @TableField(exist = false)
    private Integer myCollection;

    @ApiModelProperty("我的足迹")
    @TableField(exist = false)
    private Integer myFootPrint;
    @TableField(exist = false)
    private Integer relation;

    @ApiModelProperty("下单总金额")
    @TableField(exist = false)
    private BigDecimal orderActualPrice;

    @ApiModelProperty("返佣总金额")
    @TableField(exist = false)
    private BigDecimal multiplySum;

    @ApiModelProperty("直推间推邀请总人数")
    @TableField(exist = false)
    private Integer peopleCount;



    public MallUserEntity setBalance(BigDecimal balance) {
        this.balance = balance;
        if (BigDecimalUtil.less(this.balance, 0)) {
            throw new BusinessException("可用余额不能小于0");
        }
        return this;
    }
}
