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
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * 店铺实体
 *
 * @author 李鹏军
 * @since 2019-07-03 13:51:29
 */
@Data
@TableName("MALL_SHOPS")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MallShopsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 店铺名字
     */
    @ApiModelProperty("店铺名字")
    private String name;
    /**
     * 店铺编码
     */
    private String shopsSn;
    /**
     * 店铺图片
     */
    private String imgUrl;
    /**
     * 店铺管理员
     */
    private String userId;
    /**
     * 营业时间
     */
    private String workTime;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 详细位置
     */
    private String details;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 状态 0：逻辑删除 1：正常
     */
    @TableLogic
    private Integer deleteStatus;
    /**
     * 描述
     */
    private String shopDesc;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建人
     */
    private String createUserId;
    /**
     * 创建时间
     */
    private Date createTime;


    @ApiModelProperty("用户昵称")
    @TableField(exist = false)
    private String userNikeName;
    @ApiModelProperty("邀请用户昵称")
    @TableField(exist = false)
    private String inviteUserNikeName;
    @ApiModelProperty("总奖励")
    @TableField(exist = false)
    private BigDecimal awardTotal;
}
