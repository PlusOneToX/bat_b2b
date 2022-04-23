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
import com.platform.modules.mall.entity.MallRoomGoodsEntity;
import com.platform.modules.mall.service.MallRoomGoodsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 直播房间商品信息Controller
 *
 * @author 李鹏军
 * @since 2020-03-30 18:52:09
 */
@RestController
@RequestMapping("mall/roomgoods")
public class MallRoomGoodsController extends AbstractController {
    @Autowired
    private MallRoomGoodsService mallRoomGoodsService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallRoomGoodsEntity> list = mallRoomGoodsService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询直播房间商品信息
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:roomgoods:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallRoomGoodsService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:roomgoods:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallRoomGoodsEntity mallRoomGoods = mallRoomGoodsService.getById(id);

        return RestResponse.success().put("roomgoods", mallRoomGoods);
    }

    /**
     * 新增直播房间商品信息
     *
     * @param mallRoomGoods mallRoomGoods
     * @return RestResponse
     */
    @SysLog("新增直播房间商品信息")
    @RequestMapping("/save")
    @RequiresPermissions("mall:roomgoods:save")
    public RestResponse save(@RequestBody MallRoomGoodsEntity mallRoomGoods) {

        mallRoomGoodsService.add(mallRoomGoods);

        return RestResponse.success();
    }

    /**
     * 修改直播房间商品信息
     *
     * @param mallRoomGoods mallRoomGoods
     * @return RestResponse
     */
    @SysLog("修改直播房间商品信息")
    @RequestMapping("/update")
    @RequiresPermissions("mall:roomgoods:update")
    public RestResponse update(@RequestBody MallRoomGoodsEntity mallRoomGoods) {

        mallRoomGoodsService.update(mallRoomGoods);

        return RestResponse.success();
    }

    /**
     * 根据主键删除直播房间商品信息
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除直播房间商品信息")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:roomgoods:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallRoomGoodsService.deleteBatch(ids);

        return RestResponse.success();
    }
}
