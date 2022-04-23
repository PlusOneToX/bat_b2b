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
import com.platform.modules.mall.entity.MallKeywordsEntity;
import com.platform.modules.mall.service.MallKeywordsService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 搜索关键词Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 12:02:04
 */
@RestController
@RequestMapping("mall/keywords")
public class MallKeywordsController extends AbstractController {
    @Autowired
    private MallKeywordsService mallKeywordsService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:keywords:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallKeywordsEntity> list = mallKeywordsService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询搜索关键词
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:keywords:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallKeywordsService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:keywords:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallKeywordsEntity mallKeywords = mallKeywordsService.getById(id);

        return RestResponse.success().put("keywords", mallKeywords);
    }

    /**
     * 新增搜索关键词
     *
     * @param mallKeywords mallKeywords
     * @return RestResponse
     */
    @SysLog("新增搜索关键词")
    @RequestMapping("/save")
    @RequiresPermissions("mall:keywords:save")
    public RestResponse save(@RequestBody MallKeywordsEntity mallKeywords) {

        mallKeywordsService.add(mallKeywords);

        return RestResponse.success();
    }

    /**
     * 修改搜索关键词
     *
     * @param mallKeywords mallKeywords
     * @return RestResponse
     */
    @SysLog("修改搜索关键词")
    @RequestMapping("/update")
    @RequiresPermissions("mall:keywords:update")
    public RestResponse update(@RequestBody MallKeywordsEntity mallKeywords) {

        mallKeywordsService.update(mallKeywords);

        return RestResponse.success();
    }

    /**
     * 根据主键删除搜索关键词
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除搜索关键词")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:keywords:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallKeywordsService.deleteBatch(ids);

        return RestResponse.success();
    }
}
