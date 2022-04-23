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
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.MallCategoryEntity;
import com.platform.modules.mall.service.MallCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * @author 李鹏军
 */
@Slf4j
@RestController
@RequestMapping("/app/category")
@Api(tags = "AppCategoryController|商品分类列表接口")
public class AppCategoryController extends AppBaseController {
    @Autowired
    private MallCategoryService categoryService;

    /**
     * 商品分类列表
     */
    @IgnoreAuth
    @GetMapping("/categoryList")
    @ApiOperation(value = "商品分类列表", notes = "商品分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "level", value = "分类等级", required = true, dataType = "string", example = "1")
    })
    public RestResponse categoryList(String level) {
        //查询列表数据
        List<MallCategoryEntity> data = categoryService.list(new QueryWrapper<MallCategoryEntity>().eq(StringUtils.isNotBlank(level), "LEVEL", level).eq("IS_SHOW", 1).orderByDesc("SORT"));

        return RestResponse.success().put("data", data);
    }

    /**
     * 根据商品分类ID获取分类下的所有子分类
     *
     * @param id
     * @return
     */
    @IgnoreAuth
    @GetMapping("current")
    @ApiOperation(value = "子商品分类", notes = "根据商品分类ID获取分类下的所有子分类")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "商品分类ID", required = true, dataType = "int", example = "1")
    })
    public Object current(String id) {
        MallCategoryEntity currentCategory = categoryService.getById(id);
        //获取子分类数据
        if (null != currentCategory) {
            Map<String, Object> params = new HashMap<>(4);
            params.put("isShow", 1);
            params.put("parentId", id);
            currentCategory.setSubCategoryList(categoryService.queryAll(params));
        }
        return RestResponse.success().put("data", currentCategory);
    }
}
