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
package com.platform.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.ValidatorUtils;
import com.platform.modules.sys.entity.SysConfigEntity;
import com.platform.modules.sys.service.SysConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统配置信息
 *
 * @author 李鹏军
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController extends AbstractController {
    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 所有系统配置列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:config:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = sysConfigService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:config:info")
    public RestResponse info(@PathVariable("id") String id) {
        SysConfigEntity config = sysConfigService.getById(id);

        return RestResponse.success().put("config", config);
    }

    /**
     * 保存系统配置
     *
     * @param config config
     * @return RestResponse
     */
    @SysLog("保存系统配置")
    @PostMapping("/save")
    @RequiresPermissions("sys:config:save")
    public RestResponse save(@RequestBody SysConfigEntity config) {
        ValidatorUtils.validateEntity(config);

        sysConfigService.add(config);

        return RestResponse.success();
    }

    /**
     * 修改系统配置
     *
     * @param config config
     * @return RestResponse
     */
    @SysLog("修改系统配置")
    @PostMapping("/update")
    @RequiresPermissions("sys:config:update")
    public RestResponse update(@RequestBody SysConfigEntity config) {
        ValidatorUtils.validateEntity(config);

        sysConfigService.update(config);

        return RestResponse.success();
    }

    /**
     * 删除系统配置
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除系统配置")
    @PostMapping("/delete")
    @RequiresPermissions("sys:config:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        sysConfigService.deleteBatch(ids);

        return RestResponse.success();
    }

}
