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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallTemplateConfEntity;
import com.platform.modules.mall.service.MallTemplateConfService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 微信订阅消息Controller
 *
 * @author 李鹏军
 * @since 2019-07-04 18:19:09
 */
@RestController
@RequestMapping("mall/templateconf")
public class MallTemplateConfController extends AbstractController {
    @Autowired
    private MallTemplateConfService mallTemplateConfService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:templateconf:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallTemplateConfEntity> list = mallTemplateConfService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询微信订阅消息
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:templateconf:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        IPage page = mallTemplateConfService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:templateconf:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallTemplateConfEntity mallTemplateConf = mallTemplateConfService.getById(id);

        return RestResponse.success().put("templateconf", mallTemplateConf);
    }

    /**
     * 新增微信订阅消息
     *
     * @param mallTemplateConf mallTemplateConf
     * @return RestResponse
     */
    @SysLog("新增微信订阅消息")
    @RequestMapping("/save")
    @RequiresPermissions("mall:templateconf:save")
    public RestResponse save(@RequestBody MallTemplateConfEntity mallTemplateConf) {

        mallTemplateConfService.add(mallTemplateConf);

        return RestResponse.success();
    }

    /**
     * 修改微信订阅消息
     *
     * @param mallTemplateConf mallTemplateConf
     * @return RestResponse
     */
    @SysLog("修改微信订阅消息")
    @RequestMapping("/update")
    @RequiresPermissions("mall:templateconf:update")
    public RestResponse update(@RequestBody MallTemplateConfEntity mallTemplateConf) {

        mallTemplateConfService.update(mallTemplateConf);

        return RestResponse.success();
    }

    /**
     * 根据主键删除微信订阅消息
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除微信订阅消息")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:templateconf:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallTemplateConfService.deleteBatch(ids);

        return RestResponse.success();
    }
}
