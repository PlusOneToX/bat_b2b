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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 *
 * @author 李鹏军
 * @since 2021-01-07 10:37:39
 */
@Data
@TableName("MALL_DISTRIBUTION_CONF")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallDistributionConfEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 等级
     */
    @ApiModelProperty("等级")
    private Integer level;

    private String levelName;
    /**
     * 直推比例
     */
    @ApiModelProperty("直推比例")
    private BigDecimal direct;
    /**
     * 间推比例
     */
    @ApiModelProperty("间推比例")
    private BigDecimal indirect;
    /**
     * 1平台用户 2推广者 3店铺
     */
    private Integer type;

    @ApiModelProperty("下订单用户的等级")
    private Integer orderUserLevel;
    /**
     * 修改时间
     */
    private Date updateTime;





    @TableField(exist = false)
    private String name;
    /**
     * 会员完成订单金额满足则升级
     */
    @TableField(exist = false)
    private BigDecimal money;
    /**
     * 折扣
     */
    @TableField(exist = false)
    private BigDecimal discount;
    /**
     * 直推奖励比例
     */
    /**
     * 描述
     */
    @TableField(exist = false)
    private String description;
    /**
     * 会员升级积分标准
     */
    @TableField(exist = false)
    private String integral;
    /**
     * 等级
     */
    @TableField(exist = false)
    private Integer levelValue;
    /**
     * 会员等级图片
     */
    @TableField(exist = false)
    private String imageUrl;


}
