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
import com.platform.modules.mall.entity.MallShippingEntity;
import com.platform.modules.mall.service.MallShippingService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 快递公司配置Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 16:57:09
 */
@RestController
@RequestMapping("mall/shipping")
public class MallShippingController extends AbstractController {
    @Autowired
    private MallShippingService mallShippingService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:shipping:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallShippingEntity> list = mallShippingService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询快递公司配置
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:shipping:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallShippingService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:shipping:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallShippingEntity mallShipping = mallShippingService.getById(id);

        return RestResponse.success().put("shipping", mallShipping);
    }

    /**
     * 新增快递公司配置
     *
     * @param mallShipping mallShipping
     * @return RestResponse
     */
    @SysLog("新增快递公司配置")
    @RequestMapping("/save")
    @RequiresPermissions("mall:shipping:save")
    public RestResponse save(@RequestBody MallShippingEntity mallShipping) {

        mallShippingService.add(mallShipping);

        return RestResponse.success();
    }

    /**
     * 修改快递公司配置
     *
     * @param mallShipping mallShipping
     * @return RestResponse
     */
    @SysLog("修改快递公司配置")
    @RequestMapping("/update")
    @RequiresPermissions("mall:shipping:update")
    public RestResponse update(@RequestBody MallShippingEntity mallShipping) {

        mallShippingService.update(mallShipping);

        return RestResponse.success();
    }

    /**
     * 根据主键删除快递公司配置
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除快递公司配置")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:shipping:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallShippingService.deleteBatch(ids);

        return RestResponse.success();
    }
}
