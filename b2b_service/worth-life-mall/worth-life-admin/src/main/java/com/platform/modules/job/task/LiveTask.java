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
package com.platform.modules.job.task;

import com.alibaba.fastjson.JSONObject;
import com.platform.modules.mall.service.MallRoomAllGoodsService;
import com.platform.modules.mall.service.MallRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 直播相关定时任务
 * liveTask为spring bean的名称
 *
 * @author 李鹏军
 */
@Slf4j
@Component("liveTask")
public class LiveTask {
    @Autowired
    private MallRoomService roomService;
    @Autowired
    private MallRoomAllGoodsService roomAllGoodsService;

    /**
     * 同步直播房间列表
     */
    @SuppressWarnings(value = "unused")
    public void getLiveList(String params) {
        log.info("--------------------------开始执行同步直播房间列表任务--------------------------");
        JSONObject jsonObject = JSONObject.parseObject(params);
        Integer start = jsonObject.getInteger("start");
        Integer limit = jsonObject.getInteger("limit");
        roomService.getLiveInfo(start, limit);
        log.info("--------------------------结束执行同步直播房间列表任务--------------------------");
    }

    /**
     * 同步直播商品信息
     */
    @SuppressWarnings(value = "unused")
    public void getapproved(String params) {
        log.info("--------------------------开始执行同步直播商品信息任务--------------------------");
        JSONObject jsonObject = JSONObject.parseObject(params);
        Integer start = jsonObject.getInteger("start");
        Integer limit = jsonObject.getInteger("limit");
        roomAllGoodsService.getapproved(start, limit);
        log.info("--------------------------结束执行同步直播商品信息任务--------------------------");
    }
}
