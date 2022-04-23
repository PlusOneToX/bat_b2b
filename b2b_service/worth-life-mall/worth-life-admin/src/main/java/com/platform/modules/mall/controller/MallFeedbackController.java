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
import com.platform.modules.mall.entity.MallFeedbackEntity;
import com.platform.modules.mall.service.MallFeedbackService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 会员反馈Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 10:32:39
 */
@RestController
@RequestMapping("mall/feedback")
public class MallFeedbackController extends AbstractController {
    @Autowired
    private MallFeedbackService mallFeedbackService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:feedback:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallFeedbackEntity> list = mallFeedbackService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询会员反馈
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:feedback:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallFeedbackService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:feedback:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallFeedbackEntity mallFeedback = mallFeedbackService.getById(id);
        //设置已读
        mallFeedback.setStatus(1);
        mallFeedbackService.update(mallFeedback);
        return RestResponse.success().put("feedback", mallFeedback);
    }

    /**
     * 新增会员反馈
     *
     * @param mallFeedback mallFeedback
     * @return RestResponse
     */
    @SysLog("新增会员反馈")
    @RequestMapping("/save")
    @RequiresPermissions("mall:feedback:save")
    public RestResponse save(@RequestBody MallFeedbackEntity mallFeedback) {

        mallFeedbackService.add(mallFeedback);

        return RestResponse.success();
    }

    /**
     * 修改会员反馈
     *
     * @param mallFeedback mallFeedback
     * @return RestResponse
     */
    @SysLog("修改会员反馈")
    @RequestMapping("/update")
    @RequiresPermissions("mall:feedback:update")
    public RestResponse update(@RequestBody MallFeedbackEntity mallFeedback) {

        mallFeedbackService.update(mallFeedback);

        return RestResponse.success();
    }

    /**
     * 根据主键删除会员反馈
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除会员反馈")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:feedback:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallFeedbackService.deleteBatch(ids);

        return RestResponse.success();
    }
}
