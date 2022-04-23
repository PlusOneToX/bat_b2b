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

import cn.binarywang.wx.miniapp.bean.WxMaLiveResult;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 直播房间信息实体
 *
 * @author 李鹏军
 * @since 2020-03-30 18:52:09
 */
@Data
@TableName("MALL_ROOM")
public class MallRoomEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 房间 id
     */
    @TableId(type = IdType.INPUT)
    private Integer roomid;
    /**
     * 房间名
     */
    private String name;
    /**
     * 直播间背景图，图片规则：建议像素1080*1920，大小不超过2M
     */
    private String coverImg;
    /**
     * 直播间分享图，图片规则：建议像素800*640，大小不超过1M
     */
    private String shareImg;
    /**
     * 直播计划开始时间，列表按照 start_time 降序排列
     */
    private Long startTime;
    /**
     * 直播计划结束时间
     */
    private Long endTime;
    /**
     * 主播名
     */
    private String anchorName;
    /**
     * 直播状态 101: 直播中, 102: 未开始, 103: 已结束, 104: 禁播, 105: 暂停中, 106: 异常, 107: 已过期
     */
    private Integer liveStatus;

    /**
     * 主播名
     */
    @TableField(exist = false)
    private String anchorWechat;
    @TableField(exist = false)
    private Integer type;
    @TableField(exist = false)
    private Integer screenType;
    @TableField(exist = false)
    private Integer closeLike;
    @TableField(exist = false)
    private Integer closeGoods;
    @TableField(exist = false)
    private Integer closeComment;
    @TableField(exist = false)
    List<WxMaLiveResult.Goods> goods;
    @TableField(exist = false)
    List<MallRoomGoodsEntity> allGoods;
}
