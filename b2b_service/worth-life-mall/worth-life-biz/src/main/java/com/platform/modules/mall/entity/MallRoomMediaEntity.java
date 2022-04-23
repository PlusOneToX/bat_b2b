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
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 直播房间回放视频实体
 *
 * @author 李鹏军
 * @since 2020-03-30 18:52:08
 */
@Data
@TableName("MALL_ROOM_MEDIA")
public class MallRoomMediaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 房间 id
     */
    @TableId(type = IdType.INPUT)
    private Integer roomid;
    /**
     * 回放视频 url
     */
    private String mediaUrl;
    /**
     * 回放视频 url 过期时间
     */
    private Date expireTime;
    /**
     * 创建时间
     */
    private Date createTime;
}
