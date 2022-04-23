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
import com.platform.modules.mall.entity.MallSpecificationEntity;
import com.platform.modules.mall.service.MallSpecificationService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品SKU键表Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 23:15:44
 */
@RestController
@RequestMapping("mall/specification")
public class MallSpecificationController extends AbstractController {
    @Autowired
    private MallSpecificationService mallSpecificationService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallSpecificationEntity> list = mallSpecificationService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询SKU键
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:specification:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallSpecificationService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:specification:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallSpecificationEntity mallSpecification = mallSpecificationService.getById(id);

        return RestResponse.success().put("specification", mallSpecification);
    }

    /**
     * 新增SKU键
     *
     * @param mallSpecification mallSpecification
     * @return RestResponse
     */
    @SysLog("新增SKU键")
    @RequestMapping("/save")
    @RequiresPermissions("mall:specification:save")
    public RestResponse save(@RequestBody MallSpecificationEntity mallSpecification) {

        mallSpecificationService.add(mallSpecification);

        return RestResponse.success();
    }

    /**
     * 修改SKU键
     *
     * @param mallSpecification mallSpecification
     * @return RestResponse
     */
    @SysLog("修改SKU键")
    @RequestMapping("/update")
    @RequiresPermissions("mall:specification:update")
    public RestResponse update(@RequestBody MallSpecificationEntity mallSpecification) {

        mallSpecificationService.update(mallSpecification);

        return RestResponse.success();
    }

    /**
     * 根据主键删除SKU键
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除SKU键")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:specification:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallSpecificationService.deleteBatch(ids);

        return RestResponse.success();
    }
}
