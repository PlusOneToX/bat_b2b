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
import com.platform.modules.mall.entity.MallShopsCategoryEntity;
import com.platform.modules.mall.service.MallShopsCategoryService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 店铺商品分类Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 16:23:39
 */
@RestController
@RequestMapping("mall/shopscategory")
public class MallShopsCategoryController extends AbstractController {
    @Autowired
    private MallShopsCategoryService mallShopsCategoryService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:shopscategory:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallShopsCategoryEntity> list = mallShopsCategoryService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询店铺商品分类
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:shopscategory:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallShopsCategoryService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:shopscategory:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallShopsCategoryEntity mallShopsCategory = mallShopsCategoryService.getById(id);

        return RestResponse.success().put("shopscategory", mallShopsCategory);
    }

    /**
     * 新增店铺商品分类
     *
     * @param mallShopsCategory mallShopsCategory
     * @return RestResponse
     */
    @SysLog("新增店铺商品分类")
    @RequestMapping("/save")
    @RequiresPermissions("mall:shopscategory:save")
    public RestResponse save(@RequestBody MallShopsCategoryEntity mallShopsCategory) {

        mallShopsCategoryService.add(mallShopsCategory);

        return RestResponse.success();
    }

    /**
     * 修改店铺商品分类
     *
     * @param mallShopsCategory mallShopsCategory
     * @return RestResponse
     */
    @SysLog("修改店铺商品分类")
    @RequestMapping("/update")
    @RequiresPermissions("mall:shopscategory:update")
    public RestResponse update(@RequestBody MallShopsCategoryEntity mallShopsCategory) {

        mallShopsCategoryService.update(mallShopsCategory);

        return RestResponse.success();
    }

    /**
     * 根据主键删除店铺商品分类
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除店铺商品分类")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:shopscategory:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallShopsCategoryService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 查看我的店铺下的商品分类列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryMyShopCategory")
    public RestResponse queryMyShopCategory(@RequestParam Map<String, Object> params) {
        params.put("userId", getUserId());
        List<MallShopsCategoryEntity> list = mallShopsCategoryService.queryAll(params);

        return RestResponse.success().put("list", list);
    }
}
