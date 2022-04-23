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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.MallGoodsEntity;
import com.platform.modules.mall.service.MallBrandService;
import com.platform.modules.mall.service.MallGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李鹏军
 */
@Slf4j
@RestController
@RequestMapping("/app/brand")
@Api(tags = "AppBrandController|品牌制造商")
public class AppBrandController extends AppBaseController {
    @Autowired
    private MallBrandService brandService;
    @Autowired
    private MallGoodsService goodsService;

    /**
     * 品牌制造商列表
     */
    @IgnoreAuth
    @GetMapping("/brandList")
    @ApiOperation(value = "品牌制造商列表", notes = "品牌制造商分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse bannerList(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {

        Map<String, Object> params = new HashMap<>(8);
        params.put("page", current);
        params.put("limit", limit);
        params.put("isShow", 1);

        return RestResponse.success().put("data", brandService.queryPage(params));
    }

    /**
     * 品牌详情
     */
    @IgnoreAuth
    @GetMapping("detail")
    @ApiOperation(value = "品牌详情", notes = "根据品牌ID获取品牌详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "品牌ID", required = true, dataType = "int", example = "1")
    })
    public RestResponse detail(@RequestParam String id) {
        //查询列表数据
        return RestResponse.success().put("data", brandService.getById(id));
    }

    /**
     * 品牌制造商商品列表
     */
    @IgnoreAuth
    @GetMapping("/brandGoodsList")
    @ApiOperation(value = "品牌制造商商品列表", notes = "品牌制造商商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "brandId", value = "品牌制造商ID", example = "1", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "order", value = "排序字段", allowableValues = "RETAIL_PRICE,SALES", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "sortType", value = "排序类型", allowableValues = "ASC,DESC", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse brandGoodsList(String brandId, String order, String sortType,
                                       @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {

        Page<MallGoodsEntity> page = new Page<>(current, limit);

        IPage data = goodsService.page(page, new QueryWrapper<MallGoodsEntity>()
                .eq("BRAND_ID", brandId).eq("IS_ON_SALE", 1).orderBy(StringUtils.isNotBlank(order), "ASC".equalsIgnoreCase(sortType), order)
                .orderByDesc("SORT"));

        return RestResponse.success().put("data", data);
    }
}
