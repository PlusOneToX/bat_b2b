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
import com.platform.modules.mall.entity.MallBrandEntity;
import com.platform.modules.mall.service.MallBrandService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 品牌制造商Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 09:34:37
 */
@RestController
@RequestMapping("mall/brand")
public class MallBrandController extends AbstractController {
    @Autowired
    private MallBrandService mallBrandService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallBrandEntity> list = mallBrandService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询品牌制造商
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:brand:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallBrandService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:brand:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallBrandEntity mallBrand = mallBrandService.getById(id);

        return RestResponse.success().put("brand", mallBrand);
    }

    /**
     * 新增品牌制造商
     *
     * @param mallBrand mallBrand
     * @return RestResponse
     */
    @SysLog("新增品牌制造商")
    @RequestMapping("/save")
    @RequiresPermissions("mall:brand:save")
    public RestResponse save(@RequestBody MallBrandEntity mallBrand) {

        mallBrandService.add(mallBrand);

        return RestResponse.success();
    }

    /**
     * 修改品牌制造商
     *
     * @param mallBrand mallBrand
     * @return RestResponse
     */
    @SysLog("修改品牌制造商")
    @RequestMapping("/update")
    @RequiresPermissions("mall:brand:update")
    public RestResponse update(@RequestBody MallBrandEntity mallBrand) {

        mallBrandService.update(mallBrand);

        return RestResponse.success();
    }

    /**
     * 根据主键删除品牌制造商
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除品牌制造商")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:brand:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallBrandService.deleteBatch(ids);

        return RestResponse.success();
    }
}
