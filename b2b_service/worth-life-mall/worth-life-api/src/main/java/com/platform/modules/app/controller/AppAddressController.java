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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.MallAddressEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallAddressService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者: @author Lipengjun <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: AppAddressController <br>
 */
@Api(tags = "AppAddressController|收货地址管理控制器")
@RestController
@RequestMapping("/app/address")
public class AppAddressController {
    @Autowired
    private MallAddressService addressService;

    /**
     * 获取用户的收货地址
     */
    @GetMapping("list")
    @ApiOperation(value = "用户收货地址列表", notes = "获取当前登录用户的收货地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    })
    public RestResponse list(@LoginUser MallUserEntity user) {
        List<MallAddressEntity> addressEntities = addressService.list(new QueryWrapper<MallAddressEntity>().eq("USER_ID", user.getId()));
        return RestResponse.success().put("data", addressEntities);
    }

    /**
     * 获取收货地址的详情
     */
    @GetMapping("detail")
    @ApiOperation(value = "收货地址详情", notes = "根据地址ID获取收货地址详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "地址ID", required = true, dataType = "string", example = "1")
    })
    public RestResponse detail(@LoginUser MallUserEntity loginUser, String id) {
        MallAddressEntity entity = addressService.getById(id);
        if (null == entity || !entity.getUserId().equals(loginUser.getId())) {
            return RestResponse.error("越权操作！");
        }
        return RestResponse.success().put("data", entity);
    }

    /**
     * 添加或更新收货地址
     */
    @PostMapping("saveOrUpdate")
    @ApiOperation(value = "添加或更新收货地址", notes = "添加或更新收货地址，传ID为更新，不传ID为添加")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "id", value = "1"),
                    @ExampleProperty(mediaType = "userName", value = "1"),
                    @ExampleProperty(mediaType = "postalCode", value = "230000"),
                    @ExampleProperty(mediaType = "provinceName", value = "安徽省"),
                    @ExampleProperty(mediaType = "cityName", value = "合肥市"),
                    @ExampleProperty(mediaType = "countyName", value = "蜀山区"),
                    @ExampleProperty(mediaType = "detailInfo", value = "望江西路"),
                    @ExampleProperty(mediaType = "nationalCode", value = "340104"),
                    @ExampleProperty(mediaType = "mobile", value = "15209831990"),
                    @ExampleProperty(mediaType = "isDefault", value = "1")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse saveOrUpdate(@LoginUser MallUserEntity loginUser, @RequestBody MallAddressEntity entity) {
        entity.setUserId(loginUser.getId());
        if (StringUtils.isNullOrEmpty(entity.getId()) || "0".equals(entity.getId())) {
            addressService.save(entity);
        } else {
            addressService.update(entity);
        }
        if (entity.getIsDefault() == 1) {
            addressService.setDefaultAddress(entity.getId());
        }
        return RestResponse.success().put("data", entity);
    }

    /**
     * 同步微信小程序的地址
     */
    @PostMapping("syncAddress")
    @ApiOperation(value = "微信原生地址", notes = "同步微信小程序的地址，根据user_id,user_name,tel_number,detail_Info匹配，若匹配不到就新增一条记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "userName", value = "1"),
                    @ExampleProperty(mediaType = "postalCode", value = "230000"),
                    @ExampleProperty(mediaType = "provinceName", value = "安徽省"),
                    @ExampleProperty(mediaType = "cityName", value = "合肥市"),
                    @ExampleProperty(mediaType = "countyName", value = "蜀山区"),
                    @ExampleProperty(mediaType = "detailInfo", value = "望江西路"),
                    @ExampleProperty(mediaType = "nationalCode", value = "340104"),
                    @ExampleProperty(mediaType = "mobile", value = "15209831990"),
                    @ExampleProperty(mediaType = "isDefault", value = "1")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    public RestResponse syncAddress(@LoginUser MallUserEntity loginUser, @RequestBody MallAddressEntity entity) {
        entity.setUserId(loginUser.getId());

        //根据user_id、user_name、mobile、detail_Info查询不到就新增
        List<MallAddressEntity> addressVoList = addressService.list(new QueryWrapper<MallAddressEntity>().eq("USER_ID", entity.getUserId())
                .eq("USER_NAME", entity.getUserName()).eq("MOBILE", entity.getMobile()).eq("DETAIL_INFO", entity.getDetailInfo()));
        if (null == addressVoList || addressVoList.size() == 0) {
            entity.setIsDefault(0);
            addressService.save(entity);
        } else {
            entity.setId(addressVoList.get(0).getId());
            addressService.updateById(entity);
        }
        return RestResponse.success().put("data", entity);
    }

    /**
     * 删除指定的收货地址
     */
    @PostMapping("delete")
    @ApiOperation(value = "删除收货地址", notes = "根据地址ID删除收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "id", value = "1")
            }), required = true, dataType = "string")
    })
    public RestResponse delete(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String id = jsonParam.getString("id");

        MallAddressEntity entity = addressService.getById(id);
        if (null == entity || !entity.getUserId().equals(loginUser.getId())) {
            return RestResponse.error("越权操作！");
        }

        addressService.delete(id);
        return RestResponse.success();
    }
}
