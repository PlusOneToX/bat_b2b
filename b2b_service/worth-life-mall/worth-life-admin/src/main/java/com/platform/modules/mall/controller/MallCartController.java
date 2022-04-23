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
import com.platform.modules.mall.entity.MallCartEntity;
import com.platform.modules.mall.service.MallCartService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 购物车Controller
 *
 * @author 李鹏军
 * @since 2019-07-02 19:16:37
 */
@RestController
@RequestMapping("mall/cart")
public class MallCartController extends AbstractController {
    @Autowired
    private MallCartService mallCartService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:cart:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallCartEntity> list = mallCartService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询购物车
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:cart:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallCartService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:cart:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallCartEntity mallCart = mallCartService.getById(id);

        return RestResponse.success().put("cart", mallCart);
    }

    /**
     * 新增购物车
     *
     * @param mallCart mallCart
     * @return RestResponse
     */
    @SysLog("新增购物车")
    @RequestMapping("/save")
    @RequiresPermissions("mall:cart:save")
    public RestResponse save(@RequestBody MallCartEntity mallCart) {

        mallCartService.add(mallCart);

        return RestResponse.success();
    }

    /**
     * 修改购物车
     *
     * @param mallCart mallCart
     * @return RestResponse
     */
    @SysLog("修改购物车")
    @RequestMapping("/update")
    @RequiresPermissions("mall:cart:update")
    public RestResponse update(@RequestBody MallCartEntity mallCart) {

        mallCartService.update(mallCart);

        return RestResponse.success();
    }

    /**
     * 根据主键删除购物车
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除购物车")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:cart:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallCartService.deleteBatch(ids);

        return RestResponse.success();
    }
}
