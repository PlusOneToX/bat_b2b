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
import com.platform.modules.mall.entity.MallRewardPreEntity;
import com.platform.modules.mall.service.MallRewardPreService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 合伙人奖励预处理表Controller
 *
 * @author pfrong
 * @since 2020-11-10 16:49:41
 */
@RestController
@RequestMapping("mall/rewardpre")
public class MallRewardPreController extends AbstractController {
    @Autowired
    private MallRewardPreService mallRewardPreService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:rewardpre:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallRewardPreEntity> list = mallRewardPreService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询合伙人奖励预处理表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
//    @RequiresPermissions("mall:rewardpre:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallRewardPreService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:rewardpre:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallRewardPreEntity mallRewardPre = mallRewardPreService.getById(id);

        return RestResponse.success().put("rewardpre", mallRewardPre);
    }

    /**
     * 新增合伙人奖励预处理表
     *
     * @param mallRewardPre mallRewardPre
     * @return RestResponse
     */
    @SysLog("新增合伙人奖励预处理表")
    @RequestMapping("/save")
    @RequiresPermissions("mall:rewardpre:save")
    public RestResponse save(@RequestBody MallRewardPreEntity mallRewardPre) {

        mallRewardPreService.add(mallRewardPre);

        return RestResponse.success();
    }

    /**
     * 修改合伙人奖励预处理表
     *
     * @param mallRewardPre mallRewardPre
     * @return RestResponse
     */
    @SysLog("修改合伙人奖励预处理表")
    @RequestMapping("/update")
    @RequiresPermissions("mall:rewardpre:update")
    public RestResponse update(@RequestBody MallRewardPreEntity mallRewardPre) {

        mallRewardPreService.update(mallRewardPre);

        return RestResponse.success();
    }

    /**
     * 根据主键删除合伙人奖励预处理表
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除合伙人奖励预处理表")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:rewardpre:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallRewardPreService.deleteBatch(ids);

        return RestResponse.success();
    }
}
