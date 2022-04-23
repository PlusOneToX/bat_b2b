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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.annotation.IgnoreAuth;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallShopsCategoryEntity;
import com.platform.modules.mall.entity.MallShopsEntity;
import com.platform.modules.mall.entity.MallShopsGoodsEntity;
import com.platform.modules.mall.service.MallShopsCategoryService;
import com.platform.modules.mall.service.MallShopsGoodsService;
import com.platform.modules.mall.service.MallShopsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺
 *
 * @author 李鹏军
 */
@Slf4j
@RestController
@RequestMapping("/app/shops")
@Api(tags = "AppShopsController|店铺管理接口")
public class AppShopsController {
    @Autowired
    private MallShopsService shopsService;
    @Autowired
    private MallShopsCategoryService shopsCategoryService;
    @Autowired
    private MallShopsGoodsService shopsGoodsService;

    /**
     * 店铺列表
     */
    @IgnoreAuth
    @GetMapping("/shopsList")
    @ApiOperation(value = "店铺列表", notes = "店铺列表")
    public RestResponse shopsList() {
        List<MallShopsEntity> shopsEntityList = shopsService.list(
                new QueryWrapper<MallShopsEntity>().orderByDesc("SORT"));
        return RestResponse.success().put("data", shopsEntityList);
    }

    /**
     * 店铺详情
     */
    @IgnoreAuth
    @GetMapping("/detailBySn")
    @ApiOperation(value = "店铺详情", notes = "店铺详情")
    @ApiImplicitParam(paramType = "query", name = "shopsSn", value = "店铺SN", example = "1", required = true, dataType = "string")
    public RestResponse detailBySn(String shopsSn) {
        MallShopsEntity shopsEntity = shopsService.getOne(new QueryWrapper<MallShopsEntity>().eq("SHOPS_SN", shopsSn), false);
        if (null == shopsEntity) {
            return RestResponse.error("店铺暂未开放");
        }
        return RestResponse.success().put("data", shopsEntity);
    }

    /**
     * 根据店铺ID获取店铺商品分类
     */
    @IgnoreAuth
    @GetMapping("/shopsCategory")
    @ApiOperation(value = "店铺商品分类列表", notes = "根据店铺ID获取店铺商品分类")
    @ApiImplicitParam(paramType = "query", name = "shopsId", value = "店铺ID", example = "1", required = true, dataType = "string")
    public RestResponse shopsCategory(String shopsId) {
        List<MallShopsCategoryEntity> shopsCategoryEntityList = shopsCategoryService.list(
                new QueryWrapper<MallShopsCategoryEntity>().eq("SHOPS_ID", shopsId).eq("STATUS", 1)
                        .orderByAsc("SORT"));
        return RestResponse.success().put("data", shopsCategoryEntityList);
    }

    /**
     * 店铺商品分类列表商品
     */
    @IgnoreAuth
    @GetMapping("/shopsCategoryGoods")
    @ApiOperation(value = "店铺商品分类列表商品", notes = "根据店铺商品分类ID获取商品列表")
    @ApiImplicitParam(paramType = "query", name = "shopsCategoryId", value = "店铺商品分类ID", example = "1", required = true, dataType = "string")
    public RestResponse shopsCategoryGoods(String shopsCategoryId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("shopsCategoryId", shopsCategoryId);
        params.put("isOnSale", 1);
        List<MallShopsGoodsEntity> shopsCategoryEntityList = shopsGoodsService.queryAll(params);
        return RestResponse.success().put("data", shopsCategoryEntityList);
    }
}
