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
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 *
 * @author 李鹏军
 * @since 2021-01-04 11:44:00
 */
@Data
@TableName("MALL_USER_INTEGRAL_LOG")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallUserIntegralLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;
    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private BigDecimal amount;
    /**
     * 冻结数量
     */
    @ApiModelProperty("冻结数量")
    private BigDecimal freeze;
    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型 1直推邀请 1间推邀请 3直推购买 4间推购买  5自购白酒 6自购海报 7自购全部商品 8店铺认证 9店铺新增直推 10店铺新增间推  11积分排行奖励")
    private Integer type;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 流水id
     */
    @ApiModelProperty("流水id")
    private String pid;
    /**
     * 标注
     */
    @ApiModelProperty("标注")
    private String label;
    private Date createTime;

    /**
     * 总积分
     */
    @TableField(exist = false)
    private BigDecimal integralCount;


}
