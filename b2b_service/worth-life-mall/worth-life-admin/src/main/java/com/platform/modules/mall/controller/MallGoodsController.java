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
import com.platform.modules.mall.entity.MallGoodsEntity;
import com.platform.modules.mall.service.MallGoodsService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品表Controller
 *
 * @author 李鹏军
 * @since 2019-07-03 17:58:29
 */
@RestController
@RequestMapping("mall/goods")
public class MallGoodsController extends AbstractController {
    @Autowired
    private MallGoodsService mallGoodsService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:goods:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallGoodsEntity> list = mallGoodsService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询商品表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:goods:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallGoodsService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:goods:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallGoodsEntity mallGoods = mallGoodsService.queryById(id);

        return RestResponse.success().put("goods", mallGoods);
    }

    /**
     * 新增商品表
     *
     * @param mallGoods mallGoods
     * @return RestResponse
     */
    @SysLog("新增商品表")
    @RequestMapping("/save")
    @RequiresPermissions("mall:goods:save")
    public RestResponse save(@RequestBody MallGoodsEntity mallGoods) {
        mallGoods.setIsOnSale(0);
        mallGoodsService.add(mallGoods);

        return RestResponse.success();
    }

    /**
     * 修改商品表
     *
     * @param mallGoods mallGoods
     * @return RestResponse
     */
    @SysLog("修改商品表")
    @RequestMapping("/update")
    @RequiresPermissions("mall:goods:update")
    public RestResponse update(@RequestBody MallGoodsEntity mallGoods) {

        mallGoodsService.update(mallGoods);

        return RestResponse.success();
    }

    /**
     * 根据主键删除商品表
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除商品表")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:goods:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallGoodsService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 商品上架、下架
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("商品上架、下架")
    @RequestMapping("/changeSale")
    @RequiresPermissions("mall:goods:changeSale")
    public RestResponse changeSale(@RequestBody String[] ids) {
        mallGoodsService.changeSale(ids);

        return RestResponse.success();
    }
}
