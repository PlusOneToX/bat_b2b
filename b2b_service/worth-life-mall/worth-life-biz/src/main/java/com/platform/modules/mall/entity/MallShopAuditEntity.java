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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.checkerframework.common.aliasing.qual.NonLeaked;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author 李鹏军
 * @since 2021-01-04 18:50:28
 */
@Data
@TableName("MALL_SHOP_AUDIT")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallShopAuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String shopName;
    /**
     * 店铺地址
     */
    @ApiModelProperty("店铺地址")
    private String shopAddress;
    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;
    /**
     * 社会信用代码
     */
    @ApiModelProperty("社会信用代码")
    private String authCode;
    /**
     * 营业执照正面
     */
    @ApiModelProperty("营业执照正面")
    private String licenseFront;
    /**
     * 店铺相片
     */
    @ApiModelProperty("店铺相片")
    private String shopImag;
    /**
     * 营业执照反面
     */
    @ApiModelProperty("营业执照反面")
    private String licenseVerso;
    /**
     * 法人
     */
    @ApiModelProperty("法人")
    private String legalPerson;
    /**
     * 法人身份证号
     */
    @ApiModelProperty("法人身份证号")
    private String idCardNo;
    /**
     * 身份证正面
     */
    @ApiModelProperty("身份证正面")
    private String identityCardFront;
    /**
     * 身份证反面
     */
    @ApiModelProperty("身份证反面")
    private String identityCardVerso;
    /**
     * 0 待审核 1审核通过 2拒绝
     */
    @ApiModelProperty(" 0 待审核 1审核通过 2拒绝")
    private Integer status;
    /**
     * 审核人id
     */
    @ApiModelProperty("审核人id")
    private String auditor;
    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private Date auditTime;
    /**
     * 审核备注
     */
    @ApiModelProperty("审核备注")
    private String auditRemark;

    private String userId;

    private Date createTime;
}
