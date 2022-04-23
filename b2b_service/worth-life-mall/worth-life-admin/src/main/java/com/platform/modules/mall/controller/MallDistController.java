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
import com.platform.modules.mall.entity.MallDistEntity;
import com.platform.modules.mall.service.MallDistService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 分销Controller
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
@RestController
@RequestMapping("mall/dist")
public class MallDistController extends AbstractController {
    @Autowired
    private MallDistService mallDistService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:dist:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallDistEntity> list = mallDistService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询分销
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:dist:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallDistService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:dist:info")
    public RestResponse info(@PathVariable("id") Integer id) {
        MallDistEntity mallDist = mallDistService.queryById(id);

        return RestResponse.success().put("dist", mallDist);
    }

    /**
     * 新增分销
     *
     * @param mallDist mallDist
     * @return RestResponse
     */
    @SysLog("新增分销")
    @RequestMapping("/save")
    @RequiresPermissions("mall:dist:save")
    public RestResponse save(@RequestBody MallDistEntity mallDist) {

        mallDistService.add(mallDist);

        return RestResponse.success();
    }

    /**
     * 修改分销
     *
     * @param mallDist mallDist
     * @return RestResponse
     */
    @SysLog("修改分销")
    @RequestMapping("/update")
    @RequiresPermissions("mall:dist:update")
    public RestResponse update(@RequestBody MallDistEntity mallDist) {

        mallDistService.update(mallDist);

        return RestResponse.success();
    }

    /**
     * 根据主键删除分销
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除分销")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:dist:delete")
    public RestResponse delete(@RequestBody Integer[] ids) {
        mallDistService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 审核分销商申请
     */
    @SysLog("审核分销商申请")
    @RequestMapping("/confirmAudit/{id}")
    @RequiresPermissions("mall:dist:confirmAudit")
    public RestResponse confirmAudit(@PathVariable("id") Integer id) {
        mallDistService.confirmAudit(id);

        return RestResponse.success();
    }
}
