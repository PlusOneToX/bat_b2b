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
import com.platform.modules.mall.entity.MallFootprintEntity;
import com.platform.modules.mall.service.MallFootprintService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 会员足迹Controller
 *
 * @author 李鹏军
 * @since 2019-07-02 17:25:40
 */
@RestController
@RequestMapping("mall/footprint")
public class MallFootprintController extends AbstractController {
    @Autowired
    private MallFootprintService mallFootprintService;

    /**
     * 商品访问统计
     *
     * @param params params
     * @return RestResponse
     */
    @GetMapping("/footprintCount")
    public RestResponse footprintCount(@RequestParam Map<String, Object> params) {
        List<MallFootprintEntity> list = mallFootprintService.footprintCount(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:footprint:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallFootprintEntity> list = mallFootprintService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询会员足迹
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:footprint:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallFootprintService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:footprint:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallFootprintEntity mallFootprint = mallFootprintService.getById(id);

        return RestResponse.success().put("footprint", mallFootprint);
    }

    /**
     * 新增会员足迹
     *
     * @param mallFootprint mallFootprint
     * @return RestResponse
     */
    @SysLog("新增会员足迹")
    @RequestMapping("/save")
    @RequiresPermissions("mall:footprint:save")
    public RestResponse save(@RequestBody MallFootprintEntity mallFootprint) {

        mallFootprintService.add(mallFootprint);

        return RestResponse.success();
    }

    /**
     * 修改会员足迹
     *
     * @param mallFootprint mallFootprint
     * @return RestResponse
     */
    @SysLog("修改会员足迹")
    @RequestMapping("/update")
    @RequiresPermissions("mall:footprint:update")
    public RestResponse update(@RequestBody MallFootprintEntity mallFootprint) {

        mallFootprintService.update(mallFootprint);

        return RestResponse.success();
    }

    /**
     * 根据主键删除会员足迹
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除会员足迹")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:footprint:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallFootprintService.deleteBatch(ids);

        return RestResponse.success();
    }
}
