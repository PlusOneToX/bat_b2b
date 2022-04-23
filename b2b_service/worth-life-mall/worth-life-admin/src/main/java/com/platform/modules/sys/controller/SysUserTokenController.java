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
import com.platform.modules.sys.service.SysUserTokenService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统用户TokenController
 *
 * @author 李鹏军
 * @since 2019-02-01 11:12:49
 */
@RestController
@RequestMapping("sys/usertoken")
public class SysUserTokenController {
    @Autowired
    private SysUserTokenService sysUserTokenService;

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:usertoken:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = sysUserTokenService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 批量下线用户(删除用户token记录)
     *
     * @param userIds userIds
     * @return RestResponse
     */
    @SysLog("批量下线用户")
    @RequestMapping("/offline")
    @RequiresPermissions("sys:usertoken:offline")
    public RestResponse offline(@RequestBody String[] userIds) {
        sysUserTokenService.offlineBatch(userIds);
        return RestResponse.success();
    }
}
