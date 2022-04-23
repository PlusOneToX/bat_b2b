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
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.entity.MallRewardRecordEntity;
import com.platform.modules.mall.service.MallRewardRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 合伙人奖励记录表Controller
 *
 * @author pfrong
 * @since 2020-11-10 16:14:34
 */
@RestController
@RequestMapping("mall/rewardrecord")
public class MallRewardRecordController extends AbstractController {
    @Autowired
    private MallRewardRecordService mallRewardRecordService;
    @Autowired
    private MallUserService userService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:rewardrecord:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallRewardRecordEntity> list = mallRewardRecordService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询合伙人奖励记录表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:rewardrecord:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page<MallRewardRecordEntity> page = mallRewardRecordService.queryPage(params);
        for (MallRewardRecordEntity record : page.getRecords()) {
            String nickname = userService.getById(record.getUserId()).getNickname();
            String nickname1 = userService.getById(record.getOrderUserId()).getNickname();
            record.setUserName(nickname);
            record.setOrderUserNickName(nickname1);
        }
        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:rewardrecord:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallRewardRecordEntity mallRewardRecord = mallRewardRecordService.getById(id);

        return RestResponse.success().put("rewardrecord", mallRewardRecord);
    }

    /**
     * 新增合伙人奖励记录表
     *
     * @param mallRewardRecord mallRewardRecord
     * @return RestResponse
     */
    @SysLog("新增合伙人奖励记录表")
    @RequestMapping("/save")
    @RequiresPermissions("mall:rewardrecord:save")
    public RestResponse save(@RequestBody MallRewardRecordEntity mallRewardRecord) {

        mallRewardRecordService.add(mallRewardRecord);

        return RestResponse.success();
    }

    /**
     * 修改合伙人奖励记录表
     *
     * @param mallRewardRecord mallRewardRecord
     * @return RestResponse
     */
    @SysLog("修改合伙人奖励记录表")
    @RequestMapping("/update")
    @RequiresPermissions("mall:rewardrecord:update")
    public RestResponse update(@RequestBody MallRewardRecordEntity mallRewardRecord) {

        mallRewardRecordService.update(mallRewardRecord);

        return RestResponse.success();
    }

    /**
     * 根据主键删除合伙人奖励记录表
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除合伙人奖励记录表")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:rewardrecord:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallRewardRecordService.deleteBatch(ids);

        return RestResponse.success();
    }
}
