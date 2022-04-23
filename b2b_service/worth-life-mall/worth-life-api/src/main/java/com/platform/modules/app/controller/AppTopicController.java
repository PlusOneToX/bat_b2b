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
package com.platform.modules.app.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallTopicEntity;
import com.platform.modules.mall.service.impl.MallTopicServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者: @author Lipengjun <br>
 * 时间: 2018-08-11 08:32<br>
 * 描述: AppTopicController <br>
 */
@Api(tags = "AppTopicController|专题管理控制器")
@RestController
@RequestMapping("/app/topic")
public class AppTopicController {
    @Autowired
    private MallTopicServiceImpl mallTopicService;

    /**
     * 获取用户的收货地址
     */
    @GetMapping("list")
    @ApiOperation(value = "主题列表", notes = "主题列表")
    @IgnoreAuth
    public RestResponse list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("limit", limit);
        Page data = mallTopicService.queryPage(map);
        return RestResponse.success().put("data", data);
    }

    /**
     * 主题详情
     */
    @IgnoreAuth
    @ApiOperation(value = "主题详情", notes = "主题详情")
    @GetMapping("detail")
    public RestResponse detail(String id) {
        MallTopicEntity topicEntity = mallTopicService.getById(id);
        return RestResponse.success().put("data", topicEntity);
    }

    /**
     * 获取用户的收货地址
     */
    @GetMapping("related")
    @ApiOperation(value = "相关主题", notes = "相关主题")
    @IgnoreAuth
    public RestResponse related(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("limit", 4);
        Page data = mallTopicService.queryPage(map);
        return RestResponse.success().put("data", data);
    }
}
