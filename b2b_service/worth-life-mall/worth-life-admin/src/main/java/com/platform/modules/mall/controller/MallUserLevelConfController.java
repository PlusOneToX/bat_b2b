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
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.entity.MallUserLevelConfEntity;
import com.platform.modules.mall.service.MallUserLevelConfService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 会员等级管理Controller
 *
 * @author 李鹏军
 * @since 2021-01-07 11:06:48
 */
@RestController
@RequestMapping("mall/userlevelconf")
public class MallUserLevelConfController extends AbstractController {
    @Autowired
    private MallUserLevelConfService mallUserLevelConfService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:userlevelconf:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallUserLevelConfEntity> list = mallUserLevelConfService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询会员等级管理
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:userlevelconf:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallUserLevelConfService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:userlevelconf:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallUserLevelConfEntity mallUserLevelConf = mallUserLevelConfService.getById(id);

        return RestResponse.success().put("userlevelconf", mallUserLevelConf);
    }

    /**
     * 新增会员等级管理
     *
     * @param mallUserLevelConf mallUserLevelConf
     * @return RestResponse
     */
    @SysLog("新增会员等级管理")
    @RequestMapping("/save")
    @RequiresPermissions("mall:userlevelconf:save")
    public RestResponse save(@RequestBody MallUserLevelConfEntity mallUserLevelConf) {

        mallUserLevelConfService.add(mallUserLevelConf);

        return RestResponse.success();
    }

    /**
     * 修改会员等级管理
     *
     * @param mallUserLevelConf mallUserLevelConf
     * @return RestResponse
     */
    @SysLog("修改会员等级管理")
    @RequestMapping("/update")
    @RequiresPermissions("mall:userlevelconf:update")
    public RestResponse update(@RequestBody MallUserLevelConfEntity mallUserLevelConf) {

        mallUserLevelConfService.update(mallUserLevelConf);

        return RestResponse.success();
    }

    /**
     * 根据主键删除会员等级管理
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除会员等级管理")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:userlevelconf:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallUserLevelConfService.deleteBatch(ids);

        return RestResponse.success();
    }
}
