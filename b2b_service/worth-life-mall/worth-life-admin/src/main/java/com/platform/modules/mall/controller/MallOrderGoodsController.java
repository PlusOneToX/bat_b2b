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
import com.platform.modules.mall.entity.MallOrderGoodsEntity;
import com.platform.modules.mall.service.MallOrderGoodsService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单详情Controller
 *
 * @author 李鹏军
 * @since 2019-07-05 19:29:18
 */
@RestController
@RequestMapping("mall/ordergoods")
public class MallOrderGoodsController extends AbstractController {
    @Autowired
    private MallOrderGoodsService mallOrderGoodsService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:ordergoods:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallOrderGoodsEntity> list = mallOrderGoodsService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询订单详情
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:ordergoods:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallOrderGoodsService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:ordergoods:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallOrderGoodsEntity mallOrderGoods = mallOrderGoodsService.getById(id);

        return RestResponse.success().put("ordergoods", mallOrderGoods);
    }

    /**
     * 新增订单详情
     *
     * @param mallOrderGoods mallOrderGoods
     * @return RestResponse
     */
    @SysLog("新增订单详情")
    @RequestMapping("/save")
    @RequiresPermissions("mall:ordergoods:save")
    public RestResponse save(@RequestBody MallOrderGoodsEntity mallOrderGoods) {

        mallOrderGoodsService.add(mallOrderGoods);

        return RestResponse.success();
    }

    /**
     * 修改订单详情
     *
     * @param mallOrderGoods mallOrderGoods
     * @return RestResponse
     */
    @SysLog("修改订单详情")
    @RequestMapping("/update")
    @RequiresPermissions("mall:ordergoods:update")
    public RestResponse update(@RequestBody MallOrderGoodsEntity mallOrderGoods) {

        mallOrderGoodsService.update(mallOrderGoods);

        return RestResponse.success();
    }

    /**
     * 根据主键删除订单详情
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除订单详情")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:ordergoods:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallOrderGoodsService.deleteBatch(ids);

        return RestResponse.success();
    }
}
