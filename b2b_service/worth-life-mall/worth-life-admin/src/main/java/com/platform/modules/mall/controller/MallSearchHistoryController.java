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
import com.platform.modules.mall.entity.MallSearchHistoryEntity;
import com.platform.modules.mall.service.MallSearchHistoryService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 搜索历史Controller
 *
 * @author 李鹏军
 * @since 2019-07-02 17:44:43
 */
@RestController
@RequestMapping("mall/searchhistory")
public class MallSearchHistoryController extends AbstractController {
    @Autowired
    private MallSearchHistoryService mallSearchHistoryService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:searchhistory:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallSearchHistoryEntity> list = mallSearchHistoryService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询搜索历史
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:searchhistory:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallSearchHistoryService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:searchhistory:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallSearchHistoryEntity mallSearchHistory = mallSearchHistoryService.getById(id);

        return RestResponse.success().put("searchhistory", mallSearchHistory);
    }

    /**
     * 新增搜索历史
     *
     * @param mallSearchHistory mallSearchHistory
     * @return RestResponse
     */
    @SysLog("新增搜索历史")
    @RequestMapping("/save")
    @RequiresPermissions("mall:searchhistory:save")
    public RestResponse save(@RequestBody MallSearchHistoryEntity mallSearchHistory) {

        mallSearchHistoryService.add(mallSearchHistory);

        return RestResponse.success();
    }

    /**
     * 修改搜索历史
     *
     * @param mallSearchHistory mallSearchHistory
     * @return RestResponse
     */
    @SysLog("修改搜索历史")
    @RequestMapping("/update")
    @RequiresPermissions("mall:searchhistory:update")
    public RestResponse update(@RequestBody MallSearchHistoryEntity mallSearchHistory) {

        mallSearchHistoryService.update(mallSearchHistory);

        return RestResponse.success();
    }

    /**
     * 根据主键删除搜索历史
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除搜索历史")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:searchhistory:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallSearchHistoryService.deleteBatch(ids);

        return RestResponse.success();
    }
}
