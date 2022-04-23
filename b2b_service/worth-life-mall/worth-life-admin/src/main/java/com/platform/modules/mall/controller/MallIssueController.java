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
import com.platform.modules.mall.entity.MallIssueEntity;
import com.platform.modules.mall.service.MallIssueService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品问答Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 10:09:49
 */
@RestController
@RequestMapping("mall/issue")
public class MallIssueController extends AbstractController {
    @Autowired
    private MallIssueService mallIssueService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:issue:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallIssueEntity> list = mallIssueService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询商品问答
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:issue:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallIssueService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:issue:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallIssueEntity mallIssue = mallIssueService.getById(id);

        return RestResponse.success().put("issue", mallIssue);
    }

    /**
     * 新增商品问答
     *
     * @param mallIssue mallIssue
     * @return RestResponse
     */
    @SysLog("新增商品问答")
    @RequestMapping("/save")
    @RequiresPermissions("mall:issue:save")
    public RestResponse save(@RequestBody MallIssueEntity mallIssue) {

        mallIssueService.add(mallIssue);

        return RestResponse.success();
    }

    /**
     * 修改商品问答
     *
     * @param mallIssue mallIssue
     * @return RestResponse
     */
    @SysLog("修改商品问答")
    @RequestMapping("/update")
    @RequiresPermissions("mall:issue:update")
    public RestResponse update(@RequestBody MallIssueEntity mallIssue) {

        mallIssueService.update(mallIssue);

        return RestResponse.success();
    }

    /**
     * 根据主键删除商品问答
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除商品问答")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:issue:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallIssueService.deleteBatch(ids);

        return RestResponse.success();
    }
}
