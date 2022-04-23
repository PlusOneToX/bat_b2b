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
import com.platform.modules.mall.entity.MallGoodsSkuEntity;
import com.platform.modules.mall.service.MallGoodsSkuService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品SKUController
 *
 * @author 李鹏军
 * @since 2019-07-04 00:05:33
 */
@RestController
@RequestMapping("mall/goodssku")
public class MallGoodsSkuController extends AbstractController {
    @Autowired
    private MallGoodsSkuService mallGoodsSkuService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallGoodsSkuEntity> list = mallGoodsSkuService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询商品SKU
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:goodssku:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallGoodsSkuService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:goodssku:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallGoodsSkuEntity mallGoodsSku = mallGoodsSkuService.getById(id);

        return RestResponse.success().put("goodssku", mallGoodsSku);
    }

    /**
     * 新增商品SKU
     *
     * @param mallGoodsSku mallGoodsSku
     * @return RestResponse
     */
    @SysLog("新增商品SKU")
    @RequestMapping("/save")
    @RequiresPermissions("mall:goodssku:save")
    public RestResponse save(@RequestBody MallGoodsSkuEntity mallGoodsSku) {

        mallGoodsSkuService.add(mallGoodsSku);

        return RestResponse.success();
    }

    /**
     * 修改商品SKU
     *
     * @param mallGoodsSku mallGoodsSku
     * @return RestResponse
     */
    @SysLog("修改商品SKU")
    @RequestMapping("/update")
    @RequiresPermissions("mall:goodssku:update")
    public RestResponse update(@RequestBody MallGoodsSkuEntity mallGoodsSku) {

        mallGoodsSkuService.update(mallGoodsSku);

        return RestResponse.success();
    }

    /**
     * 根据主键删除商品SKU
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除商品SKU")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:goodssku:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallGoodsSkuService.deleteBatch(ids);

        return RestResponse.success();
    }


    /**
     * 根据goodsId查询商品
     *
     * @param goodsId
     * @return
     */
    @RequestMapping("/queryByGoodsId/{goodsId}")
    public RestResponse queryByGoodsId(@PathVariable("goodsId") String goodsId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("goodsId", goodsId);
        List<MallGoodsSkuEntity> list = mallGoodsSkuService.queryAll(params);

        return RestResponse.success().put("list", list);
    }


    /**
     * 保存
     */
    @SysLog("新增商品SKU")
    @RequestMapping("/saveGoodsProduct")
    public RestResponse saveGoodsProduct(@RequestBody List<MallGoodsSkuEntity> productList) {
        mallGoodsSkuService.saveGoodsProduct(productList);

        return RestResponse.success();
    }
}
