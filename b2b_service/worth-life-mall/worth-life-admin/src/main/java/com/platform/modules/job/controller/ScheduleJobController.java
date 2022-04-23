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
package com.platform.modules.job.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.ValidatorUtils;
import com.platform.modules.job.entity.ScheduleJobEntity;
import com.platform.modules.job.service.ScheduleJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 定时任务
 *
 * @author 李鹏军
 */
@RestController
@RequestMapping("/sys/schedule")
public class ScheduleJobController {
    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 分页查询定时任务
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:schedule:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = scheduleJobService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param jobId 主键
     * @return RestResponse
     */
    @GetMapping("/info/{jobId}")
    @RequiresPermissions("sys:schedule:info")
    public RestResponse info(@PathVariable("jobId") String jobId) {
        ScheduleJobEntity schedule = scheduleJobService.getById(jobId);

        return RestResponse.success().put("schedule", schedule);
    }

    /**
     * 新增定时任务
     *
     * @param scheduleJob scheduleJob
     * @return RestResponse
     */
    @SysLog("新增定时任务")
    @PostMapping("/save")
    @RequiresPermissions("sys:schedule:save")
    public RestResponse save(@RequestBody ScheduleJobEntity scheduleJob) {
        ValidatorUtils.validateEntity(scheduleJob);

        scheduleJobService.add(scheduleJob);

        return RestResponse.success();
    }

    /**
     * 修改定时任务
     *
     * @param scheduleJob scheduleJob
     * @return RestResponse
     */
    @SysLog("修改定时任务")
    @PostMapping("/update")
    @RequiresPermissions("sys:schedule:update")
    public RestResponse update(@RequestBody ScheduleJobEntity scheduleJob) {
        ValidatorUtils.validateEntity(scheduleJob);

        scheduleJobService.update(scheduleJob);

        return RestResponse.success();
    }

    /**
     * 删除定时任务
     *
     * @param jobIds jobIds
     * @return RestResponse
     */
    @SysLog("删除定时任务")
    @PostMapping("/delete")
    @RequiresPermissions("sys:schedule:delete")
    public RestResponse delete(@RequestBody String[] jobIds) {
        scheduleJobService.deleteBatch(jobIds);

        return RestResponse.success();
    }

    /**
     * 立即执行任务
     *
     * @param jobIds jobIds
     * @return RestResponse
     */
    @SysLog("立即执行任务")
    @PostMapping("/run")
    @RequiresPermissions("sys:schedule:run")
    public RestResponse run(@RequestBody String[] jobIds) {
        scheduleJobService.run(jobIds);

        return RestResponse.success();
    }

    /**
     * 暂停定时任务
     *
     * @param jobIds jobIds
     * @return RestResponse
     */
    @SysLog("暂停定时任务")
    @PostMapping("/pause")
    @RequiresPermissions("sys:schedule:pause")
    public RestResponse pause(@RequestBody String[] jobIds) {
        scheduleJobService.pause(jobIds);

        return RestResponse.success();
    }

    /**
     * 恢复定时任务
     *
     * @param jobIds jobIds
     * @return RestResponse
     */
    @SysLog("恢复定时任务")
    @PostMapping("/resume")
    @RequiresPermissions("sys:schedule:resume")
    public RestResponse resume(@RequestBody String[] jobIds) {
        scheduleJobService.resume(jobIds);

        return RestResponse.success();
    }
}
