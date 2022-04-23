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
package com.platform.modules.wx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.wx.entity.WxMaConfigEntity;
import com.platform.modules.wx.service.WxMaConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 微信小程序配置Controller
 *
 * @author 李鹏军
 * @since 2020-04-05 21:58:47
 */
@RestController
@RequestMapping("wx/maconfig")
public class WxMaConfigController extends AbstractController {
    @Autowired
    private WxMaConfigService wxMaConfigService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("wx:maconfig:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<WxMaConfigEntity> list = wxMaConfigService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询微信小程序配置
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("wx:maconfig:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = wxMaConfigService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("wx:maconfig:info")
    public RestResponse info(@PathVariable("id") String id) {
        WxMaConfigEntity wxMaConfig = wxMaConfigService.getById(id);

        return RestResponse.success().put("maconfig", wxMaConfig);
    }

    /**
     * 新增微信小程序配置
     *
     * @param wxMaConfig wxMaConfig
     * @return RestResponse
     */
    @SysLog("新增微信小程序配置")
    @RequestMapping("/save")
    @RequiresPermissions("wx:maconfig:save")
    public RestResponse save(@RequestBody WxMaConfigEntity wxMaConfig) {

        wxMaConfigService.add(wxMaConfig);

        return RestResponse.success();
    }

    /**
     * 修改微信小程序配置
     *
     * @param wxMaConfig wxMaConfig
     * @return RestResponse
     */
    @SysLog("修改微信小程序配置")
    @RequestMapping("/update")
    @RequiresPermissions("wx:maconfig:update")
    public RestResponse update(@RequestBody WxMaConfigEntity wxMaConfig) {

        wxMaConfigService.update(wxMaConfig);

        return RestResponse.success();
    }

    /**
     * 根据主键删除微信小程序配置
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除微信小程序配置")
    @RequestMapping("/delete")
    @RequiresPermissions("wx:maconfig:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        wxMaConfigService.deleteBatch(ids);

        return RestResponse.success();
    }
}
