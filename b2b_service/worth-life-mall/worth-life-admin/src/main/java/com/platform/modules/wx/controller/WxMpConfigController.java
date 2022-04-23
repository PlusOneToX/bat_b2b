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
import com.platform.modules.wx.entity.WxMpConfigEntity;
import com.platform.modules.wx.service.WxMpConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 微信公众号配置Controller
 *
 * @author 李鹏军
 * @since 2020-04-05 21:58:48
 */
@RestController
@RequestMapping("wx/mpconfig")
public class WxMpConfigController extends AbstractController {
    @Autowired
    private WxMpConfigService wxMpConfigService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("wx:mpconfig:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<WxMpConfigEntity> list = wxMpConfigService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询微信公众号配置
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("wx:mpconfig:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = wxMpConfigService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("wx:mpconfig:info")
    public RestResponse info(@PathVariable("id") String id) {
        WxMpConfigEntity wxMpConfig = wxMpConfigService.getById(id);

        return RestResponse.success().put("mpconfig", wxMpConfig);
    }

    /**
     * 新增微信公众号配置
     *
     * @param wxMpConfig wxMpConfig
     * @return RestResponse
     */
    @SysLog("新增微信公众号配置")
    @RequestMapping("/save")
    @RequiresPermissions("wx:mpconfig:save")
    public RestResponse save(@RequestBody WxMpConfigEntity wxMpConfig) {

        wxMpConfigService.add(wxMpConfig);

        return RestResponse.success();
    }

    /**
     * 修改微信公众号配置
     *
     * @param wxMpConfig wxMpConfig
     * @return RestResponse
     */
    @SysLog("修改微信公众号配置")
    @RequestMapping("/update")
    @RequiresPermissions("wx:mpconfig:update")
    public RestResponse update(@RequestBody WxMpConfigEntity wxMpConfig) {

        wxMpConfigService.update(wxMpConfig);

        return RestResponse.success();
    }

    /**
     * 根据主键删除微信公众号配置
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除微信公众号配置")
    @RequestMapping("/delete")
    @RequiresPermissions("wx:mpconfig:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        wxMpConfigService.deleteBatch(ids);

        return RestResponse.success();
    }
}
