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
import com.platform.modules.mall.entity.MallUserSignRecordEntity;
import com.platform.modules.mall.service.MallUserSignRecordService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户签到记录Controller
 *
 * @author 李鹏军
 * @since 2019-07-02 18:15:12
 */
@RestController
@RequestMapping("mall/usersignrecord")
public class MallUserSignRecordController extends AbstractController {
    @Autowired
    private MallUserSignRecordService mallUserSignRecordService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:usersignrecord:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallUserSignRecordEntity> list = mallUserSignRecordService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询用户签到记录
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:usersignrecord:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallUserSignRecordService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:usersignrecord:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallUserSignRecordEntity mallUserSignRecord = mallUserSignRecordService.getById(id);

        return RestResponse.success().put("usersignrecord", mallUserSignRecord);
    }

    /**
     * 新增用户签到记录
     *
     * @param mallUserSignRecord mallUserSignRecord
     * @return RestResponse
     */
    @SysLog("新增用户签到记录")
    @RequestMapping("/save")
    @RequiresPermissions("mall:usersignrecord:save")
    public RestResponse save(@RequestBody MallUserSignRecordEntity mallUserSignRecord) {

        mallUserSignRecordService.add(mallUserSignRecord);

        return RestResponse.success();
    }

    /**
     * 修改用户签到记录
     *
     * @param mallUserSignRecord mallUserSignRecord
     * @return RestResponse
     */
    @SysLog("修改用户签到记录")
    @RequestMapping("/update")
    @RequiresPermissions("mall:usersignrecord:update")
    public RestResponse update(@RequestBody MallUserSignRecordEntity mallUserSignRecord) {

        mallUserSignRecordService.update(mallUserSignRecord);

        return RestResponse.success();
    }

    /**
     * 根据主键删除用户签到记录
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除用户签到记录")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:usersignrecord:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallUserSignRecordService.deleteBatch(ids);

        return RestResponse.success();
    }
}
