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
import com.platform.modules.mall.entity.MallSeckillEntity;
import com.platform.modules.mall.service.MallSeckillService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 秒杀库存表Controller
 *
 * @author 李鹏军
 * @since 2019-07-07 12:05:21
 */
@RestController
@RequestMapping("mall/seckill")
public class MallSeckillController extends AbstractController {
    @Autowired
    private MallSeckillService mallSeckillService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:seckill:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallSeckillEntity> list = mallSeckillService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询秒杀库存表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:seckill:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallSeckillService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:seckill:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallSeckillEntity mallSeckill = mallSeckillService.getById(id);

        return RestResponse.success().put("seckill", mallSeckill);
    }

    /**
     * 新增秒杀库存表
     *
     * @param mallSeckill mallSeckill
     * @return RestResponse
     */
    @SysLog("新增秒杀库存表")
    @RequestMapping("/save")
    @RequiresPermissions("mall:seckill:save")
    public RestResponse save(@RequestBody MallSeckillEntity mallSeckill) {

        mallSeckillService.add(mallSeckill);

        return RestResponse.success();
    }

    /**
     * 修改秒杀库存表
     *
     * @param mallSeckill mallSeckill
     * @return RestResponse
     */
    @SysLog("修改秒杀库存表")
    @RequestMapping("/update")
    @RequiresPermissions("mall:seckill:update")
    public RestResponse update(@RequestBody MallSeckillEntity mallSeckill) {

        mallSeckillService.update(mallSeckill);

        return RestResponse.success();
    }

    /**
     * 根据主键删除秒杀库存表
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除秒杀库存表")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:seckill:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallSeckillService.deleteBatch(ids);

        return RestResponse.success();
    }
}
