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
import com.platform.modules.mall.entity.MallCollectEntity;
import com.platform.modules.mall.service.MallCollectService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 会员收藏Controller
 *
 * @author 李鹏军
 * @since 2019-07-02 14:51:33
 */
@RestController
@RequestMapping("mall/collect")
public class MallCollectController extends AbstractController {
    @Autowired
    private MallCollectService mallCollectService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:collect:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallCollectEntity> list = mallCollectService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询会员收藏
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:collect:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallCollectService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:collect:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallCollectEntity mallCollect = mallCollectService.getById(id);

        return RestResponse.success().put("collect", mallCollect);
    }

    /**
     * 新增会员收藏
     *
     * @param mallCollect mallCollect
     * @return RestResponse
     */
    @SysLog("新增会员收藏")
    @RequestMapping("/save")
    @RequiresPermissions("mall:collect:save")
    public RestResponse save(@RequestBody MallCollectEntity mallCollect) {

        mallCollectService.add(mallCollect);

        return RestResponse.success();
    }

    /**
     * 修改会员收藏
     *
     * @param mallCollect mallCollect
     * @return RestResponse
     */
    @SysLog("修改会员收藏")
    @RequestMapping("/update")
    @RequiresPermissions("mall:collect:update")
    public RestResponse update(@RequestBody MallCollectEntity mallCollect) {

        mallCollectService.update(mallCollect);

        return RestResponse.success();
    }

    /**
     * 根据主键删除会员收藏
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除会员收藏")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:collect:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallCollectService.deleteBatch(ids);

        return RestResponse.success();
    }
}
