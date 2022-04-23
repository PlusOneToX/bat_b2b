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
package com.platform.modules.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallCategoryEntity;
import com.platform.modules.mall.service.MallCategoryService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品分类Controller
 *
 * @author 李鹏军
 * @since 2019-07-02 19:53:21
 */
@RestController
@RequestMapping("mall/category")
public class MallCategoryController extends AbstractController {
    @Autowired
    private MallCategoryService mallCategoryService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:category:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallCategoryEntity> list = mallCategoryService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询商品分类
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:category:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallCategoryService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:category:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallCategoryEntity mallCategory = mallCategoryService.getById(id);

        return RestResponse.success().put("category", mallCategory);
    }

    /**
     * 新增商品分类
     *
     * @param mallCategory mallCategory
     * @return RestResponse
     */
    @SysLog("新增商品分类")
    @RequestMapping("/save")
    @RequiresPermissions("mall:category:save")
    public RestResponse save(@RequestBody MallCategoryEntity mallCategory) {

        mallCategoryService.add(mallCategory);

        return RestResponse.success();
    }

    /**
     * 修改商品分类
     *
     * @param mallCategory mallCategory
     * @return RestResponse
     */
    @SysLog("修改商品分类")
    @RequestMapping("/update")
    @RequiresPermissions("mall:category:update")
    public RestResponse update(@RequestBody MallCategoryEntity mallCategory) {

        mallCategoryService.update(mallCategory);

        return RestResponse.success();
    }

    /**
     * 根据主键删除商品分类
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除商品分类")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:category:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallCategoryService.delete(ids);

        return RestResponse.success();
    }
}
