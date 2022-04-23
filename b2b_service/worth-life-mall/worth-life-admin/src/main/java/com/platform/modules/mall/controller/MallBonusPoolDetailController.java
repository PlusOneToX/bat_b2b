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
import com.platform.modules.mall.entity.MallUserBonusDetailEntity;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.entity.MallBonusPoolDetailEntity;
import com.platform.modules.mall.service.MallBonusPoolDetailService;
import com.platform.utils.ExcelUtils;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 李鹏军
 * @since 2021-01-05 16:03:10
 */
@RestController
@RequestMapping("mall/bonuspooldetail")
public class MallBonusPoolDetailController extends AbstractController {
    @Autowired
    private MallBonusPoolDetailService mallBonusPoolDetailService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:bonuspooldetail:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallBonusPoolDetailEntity> list = mallBonusPoolDetailService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:bonuspooldetail:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallBonusPoolDetailService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:bonuspooldetail:info")
    public RestResponse info(@PathVariable("id") Integer id) {
        MallBonusPoolDetailEntity mallBonusPoolDetail = mallBonusPoolDetailService.getById(id);

        return RestResponse.success().put("bonuspooldetail", mallBonusPoolDetail);
    }

    /**
     * 新增
     *
     * @param mallBonusPoolDetail mallBonusPoolDetail
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("mall:bonuspooldetail:save")
    public RestResponse save(@RequestBody MallBonusPoolDetailEntity mallBonusPoolDetail) {

        mallBonusPoolDetailService.add(mallBonusPoolDetail);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param mallBonusPoolDetail mallBonusPoolDetail
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("mall:bonuspooldetail:update")
    public RestResponse update(@RequestBody MallBonusPoolDetailEntity mallBonusPoolDetail) {

        mallBonusPoolDetailService.update(mallBonusPoolDetail);

        return RestResponse.success();
    }

    /**
     * 根据主键删除
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:bonuspooldetail:delete")
    public RestResponse delete(@RequestBody Integer[] ids) {
        mallBonusPoolDetailService.deleteBatch(ids);

        return RestResponse.success();
    }




}
