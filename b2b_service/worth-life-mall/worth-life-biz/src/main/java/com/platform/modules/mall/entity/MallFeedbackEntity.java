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
 * 会员反馈实体
 *
 * @author 李鹏军
 * @since 2019-07-03 10:32:39
 */
@Data
@TableName("MALL_FEEDBACK")
public class MallFeedbackEntity implements Serializable {
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
     * 手机
     */
    private String mobile;
    /**
     * 反馈类型 1:商品相关, 2:物流状况, 3:客户服务,4:优惠活动, 5:功能异常, 6:产品建议, 7:其他
     */
    private Integer feedType;
    /**
     * 详细内容
     */
    private String content;
    /**
     * 状态 0:未读 1:已读
     */
    private Integer status;
    /**
     * 反馈时间
     */
    private Date addTime;
    /**
     * 微信昵称
     */
    @TableField(exist = false)
    private String nickname;
}
