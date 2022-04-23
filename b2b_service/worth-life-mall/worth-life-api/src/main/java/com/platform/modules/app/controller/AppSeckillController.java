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

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.SeckillStatEnum;
import com.platform.modules.mall.entity.MallSeckillEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallSeckillService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 秒杀
 *
 * @author 李鹏军
 */
@Slf4j
@RestController
@RequestMapping("/app/seckill")
@Api(tags = "AppSeckillController|商品秒杀接口")
public class AppSeckillController {
    @Autowired
    private MallSeckillService seckillService;

    @ApiOperation(value = "参与秒杀", notes = "秒杀接口", response = RestResponse.class)
    @PostMapping("/startSeckill")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "seckillId", value = "111"),
                    @ExampleProperty(mediaType = "fromType", value = "用户下单来源类型 1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序")
            }), required = true, dataType = "string")
    })
    public RestResponse startSeckill(@LoginUser MallUserEntity userEntity, @RequestBody JSONObject jsonParam) {
        String seckillId = jsonParam.getString("seckillId");
        Integer fromType = jsonParam.getInteger("fromType");

        log.info("开始秒杀，用户:{}{}", userEntity.getId(), seckillId);

        RestResponse result = seckillService.startSeckill(seckillId, userEntity.getId(), fromType);
        // 限流生效，返回null
        if (result == null) {
            RestResponse.error(SeckillStatEnum.MUCH.getInfo());
        }
        return result;
    }

    @IgnoreAuth
    @ApiOperation(value = "秒杀活动列表", notes = "秒杀活动列表", response = RestResponse.class)
    @GetMapping("/list")
    public RestResponse list() {
        Map<String, Object> params = new HashMap<>(4);
        params.put("endTime", new Date());

        List<MallSeckillEntity> data = seckillService.queryAll(params);
        return RestResponse.success().put("data", data);
    }
}
