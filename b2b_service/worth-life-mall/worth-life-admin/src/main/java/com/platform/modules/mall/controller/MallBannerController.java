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
import com.platform.modules.mall.entity.MallBannerEntity;
import com.platform.modules.mall.service.MallBannerService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 首页轮播配置Controller
 *
 * @author 李鹏军
 * @since 2019-07-01 10:57:03
 */
@RestController
@RequestMapping("mall/banner")
public class MallBannerController extends AbstractController {
    @Autowired
    private MallBannerService mallBannerService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:banner:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallBannerEntity> list = mallBannerService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询首页轮播配置
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:banner:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallBannerService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:banner:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallBannerEntity mallBanner = mallBannerService.getById(id);

        return RestResponse.success().put("banner", mallBanner);
    }

    /**
     * 新增首页轮播配置
     *
     * @param mallBanner mallBanner
     * @return RestResponse
     */
    @SysLog("新增首页轮播配置")
    @RequestMapping("/save")
    @RequiresPermissions("mall:banner:save")
    public RestResponse save(@RequestBody MallBannerEntity mallBanner) {

        mallBannerService.add(mallBanner);

        return RestResponse.success();
    }

    /**
     * 修改首页轮播配置
     *
     * @param mallBanner mallBanner
     * @return RestResponse
     */
    @SysLog("修改首页轮播配置")
    @RequestMapping("/update")
    @RequiresPermissions("mall:banner:update")
    public RestResponse update(@RequestBody MallBannerEntity mallBanner) {

        mallBannerService.update(mallBanner);

        return RestResponse.success();
    }

    /**
     * 根据主键删除首页轮播配置
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除首页轮播配置")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:banner:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallBannerService.deleteBatch(ids);

        return RestResponse.success();
    }
}
