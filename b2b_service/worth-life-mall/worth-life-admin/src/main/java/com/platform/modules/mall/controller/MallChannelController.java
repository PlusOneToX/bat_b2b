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
import com.platform.modules.mall.entity.MallChannelEntity;
import com.platform.modules.mall.service.MallChannelService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 首页展示分类Controller
 *
 * @author 李鹏军
 * @since 2019-07-02 21:45:27
 */
@RestController
@RequestMapping("mall/channel")
public class MallChannelController extends AbstractController {
    @Autowired
    private MallChannelService mallChannelService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:channel:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallChannelEntity> list = mallChannelService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询首页展示分类
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:channel:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallChannelService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:channel:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallChannelEntity mallChannel = mallChannelService.getById(id);

        return RestResponse.success().put("channel", mallChannel);
    }

    /**
     * 新增首页展示分类
     *
     * @param mallChannel mallChannel
     * @return RestResponse
     */
    @SysLog("新增首页展示分类")
    @RequestMapping("/save")
    @RequiresPermissions("mall:channel:save")
    public RestResponse save(@RequestBody MallChannelEntity mallChannel) {

        mallChannelService.add(mallChannel);

        return RestResponse.success();
    }

    /**
     * 修改首页展示分类
     *
     * @param mallChannel mallChannel
     * @return RestResponse
     */
    @SysLog("修改首页展示分类")
    @RequestMapping("/update")
    @RequiresPermissions("mall:channel:update")
    public RestResponse update(@RequestBody MallChannelEntity mallChannel) {

        mallChannelService.update(mallChannel);

        return RestResponse.success();
    }

    /**
     * 根据主键删除首页展示分类
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除首页展示分类")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:channel:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallChannelService.deleteBatch(ids);

        return RestResponse.success();
    }
}
