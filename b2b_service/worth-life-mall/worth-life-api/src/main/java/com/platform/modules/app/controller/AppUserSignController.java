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

import com.platform.annotation.LoginUser;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallUserSignRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 用户签到控制器
 *
 * @author 李鹏军
 */
@Api(tags = "ApiSignController|用户签到控制器")
@RestController
@RequestMapping("/app/sign")
public class AppUserSignController {
    @Autowired
    MallUserSignRecordService userSignService;

    /**
     * 获取当月的签到记录和次数
     */
    @GetMapping("getMonthSign")
    @ApiOperation(value = "签到记录和次数", notes = "获取用户当月的签到记录和次数")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    })
    public RestResponse getMonthSign(@LoginUser MallUserEntity loginUser) {
        //查询当前月份
        Map<String, Object> resultMap = userSignService.getSignByParams(loginUser.getId());

        return RestResponse.success(resultMap);
    }

    /**
     * 用户当天是否签到
     */
    @GetMapping("nowDaySigned")
    @ApiOperation(value = "是否签到", notes = "用户当天是否签到")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    })
    public RestResponse nowDaySigned(@LoginUser MallUserEntity loginUser) {
        boolean signed = userSignService.nowDaySigned(loginUser.getId());
        return RestResponse.success().put("data", signed);
    }

    /**
     * 用户签到，返回本次签到获取的积分
     */
    @GetMapping("userSign")
    @ApiOperation(value = "签到", notes = "用户签到，返回本次签到获取的积分")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    })
    public RestResponse userSign(@LoginUser MallUserEntity loginUser) {
        BigDecimal integral = userSignService.userSign(loginUser.getId());
        return RestResponse.success().put("data", integral);
    }
}
