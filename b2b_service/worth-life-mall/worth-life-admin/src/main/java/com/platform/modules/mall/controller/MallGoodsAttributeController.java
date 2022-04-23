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
import com.platform.modules.mall.entity.MallGoodsAttributeEntity;
import com.platform.modules.mall.service.MallGoodsAttributeService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品参数列表Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 22:33:20
 */
@RestController
@RequestMapping("mall/goodsattribute")
public class MallGoodsAttributeController extends AbstractController {
    @Autowired
    private MallGoodsAttributeService mallGoodsAttributeService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallGoodsAttributeEntity> list = mallGoodsAttributeService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询商品参数列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:goodsattribute:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallGoodsAttributeService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:goodsattribute:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallGoodsAttributeEntity mallGoodsAttribute = mallGoodsAttributeService.getById(id);

        return RestResponse.success().put("goodsattribute", mallGoodsAttribute);
    }

    /**
     * 新增商品参数列表
     *
     * @param mallGoodsAttribute mallGoodsAttribute
     * @return RestResponse
     */
    @SysLog("新增商品参数列表")
    @RequestMapping("/save")
    @RequiresPermissions("mall:goodsattribute:save")
    public RestResponse save(@RequestBody MallGoodsAttributeEntity mallGoodsAttribute) {

        mallGoodsAttributeService.add(mallGoodsAttribute);

        return RestResponse.success();
    }

    /**
     * 修改商品参数列表
     *
     * @param mallGoodsAttribute mallGoodsAttribute
     * @return RestResponse
     */
    @SysLog("修改商品参数列表")
    @RequestMapping("/update")
    @RequiresPermissions("mall:goodsattribute:update")
    public RestResponse update(@RequestBody MallGoodsAttributeEntity mallGoodsAttribute) {

        mallGoodsAttributeService.update(mallGoodsAttribute);

        return RestResponse.success();
    }

    /**
     * 根据主键删除商品参数列表
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除商品参数列表")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:goodsattribute:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallGoodsAttributeService.deleteBatch(ids);

        return RestResponse.success();
    }
}
