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
import com.platform.modules.mall.entity.MallShopsGoodsEntity;
import com.platform.modules.mall.service.MallShopsGoodsService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 店铺与商品关联关系Controller
 *
 * @author 李鹏军
 * @since 2019-07-04 09:46:59
 */
@RestController
@RequestMapping("mall/shopsgoods")
public class MallShopsGoodsController extends AbstractController {
    @Autowired
    private MallShopsGoodsService mallShopsGoodsService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:shopsgoods:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallShopsGoodsEntity> list = mallShopsGoodsService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询店铺与商品关联关系
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:shopsgoods:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallShopsGoodsService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:shopsgoods:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallShopsGoodsEntity mallShopsGoods = mallShopsGoodsService.getById(id);

        return RestResponse.success().put("shopsgoods", mallShopsGoods);
    }

    /**
     * 新增店铺与商品关联关系
     *
     * @param mallShopsGoods mallShopsGoods
     * @return RestResponse
     */
    @SysLog("新增店铺与商品关联关系")
    @RequestMapping("/save")
    @RequiresPermissions("mall:shopsgoods:save")
    public RestResponse save(@RequestBody MallShopsGoodsEntity mallShopsGoods) {

        mallShopsGoodsService.add(mallShopsGoods);

        return RestResponse.success();
    }

    /**
     * 修改店铺与商品关联关系
     *
     * @param mallShopsGoods mallShopsGoods
     * @return RestResponse
     */
    @SysLog("修改店铺与商品关联关系")
    @RequestMapping("/update")
    @RequiresPermissions("mall:shopsgoods:update")
    public RestResponse update(@RequestBody MallShopsGoodsEntity mallShopsGoods) {

        mallShopsGoodsService.update(mallShopsGoods);

        return RestResponse.success();
    }

    /**
     * 根据主键删除店铺与商品关联关系
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除店铺与商品关联关系")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:shopsgoods:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallShopsGoodsService.deleteBatch(ids);

        return RestResponse.success();
    }
}
