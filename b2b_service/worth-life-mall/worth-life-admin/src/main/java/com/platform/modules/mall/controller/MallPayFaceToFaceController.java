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
import com.platform.modules.mall.entity.MallPayFaceToFaceEntity;
import com.platform.modules.mall.service.MallPayFaceToFaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 当面付记录Controller
 *
 * @author 李鹏军
 * @since 2020-08-12 16:31:15
 */
@RestController
@RequestMapping("mall/payfacetoface")
public class MallPayFaceToFaceController extends AbstractController {
    @Autowired
    private MallPayFaceToFaceService mallPayFaceToFaceService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:payfacetoface:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallPayFaceToFaceEntity> list = mallPayFaceToFaceService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询当面付记录
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:payfacetoface:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallPayFaceToFaceService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:payfacetoface:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallPayFaceToFaceEntity mallPayFaceToFace = mallPayFaceToFaceService.getById(id);

        return RestResponse.success().put("payfacetoface", mallPayFaceToFace);
    }
}
