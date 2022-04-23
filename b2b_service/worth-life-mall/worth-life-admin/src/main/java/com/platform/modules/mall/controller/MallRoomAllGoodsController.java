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
import com.platform.modules.mall.entity.MallRoomAllGoodsEntity;
import com.platform.modules.mall.service.MallRoomAllGoodsService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 直播接口所有商品信息Controller
 *
 * @author 李鹏军
 * @since 2020-07-06 16:35:39
 */
@RestController
@RequestMapping("mall/roomallgoods")
public class MallRoomAllGoodsController extends AbstractController {
    @Autowired
    private MallRoomAllGoodsService mallRoomAllGoodsService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:roomallgoods:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallRoomAllGoodsEntity> list = mallRoomAllGoodsService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询直播接口所有商品信息
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:roomallgoods:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallRoomAllGoodsService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param goodsId 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{goodsId}")
    @RequiresPermissions("mall:roomallgoods:info")
    public RestResponse info(@PathVariable("goodsId") Integer goodsId) {
        MallRoomAllGoodsEntity mallRoomAllGoods = mallRoomAllGoodsService.getById(goodsId);

        return RestResponse.success().put("roomallgoods", mallRoomAllGoods);
    }

    /**
     * 新增直播接口所有商品信息
     *
     * @param mallRoomAllGoods mallRoomAllGoods
     * @return RestResponse
     */
    @SysLog("新增直播商品信息")
    @RequestMapping("/save")
    @RequiresPermissions("mall:roomallgoods:save")
    public RestResponse save(@RequestBody MallRoomAllGoodsEntity mallRoomAllGoods) {
        boolean flag = mallRoomAllGoodsService.add(mallRoomAllGoods);

        return flag ? RestResponse.success() : RestResponse.error("操作失败");
    }

    /**
     * 修改直播接口所有商品信息
     *
     * @param mallRoomAllGoods mallRoomAllGoods
     * @return RestResponse
     */
    @SysLog("修改直播商品信息")
    @RequestMapping("/update")
    @RequiresPermissions("mall:roomallgoods:update")
    public RestResponse update(@RequestBody MallRoomAllGoodsEntity mallRoomAllGoods) {
        boolean flag = mallRoomAllGoodsService.update(mallRoomAllGoods);

        return flag ? RestResponse.success() : RestResponse.error("操作失败");
    }

    /**
     * 根据主键删除直播接口所有商品信息
     *
     * @param goodsIds goodsIds
     * @return RestResponse
     */
    @SysLog("删除直播商品信息")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:roomallgoods:delete")
    public RestResponse delete(@RequestBody Integer[] goodsIds) {
        boolean flag = mallRoomAllGoodsService.deleteBatch(goodsIds);
        return flag ? RestResponse.success() : RestResponse.error("操作失败");
    }

    /**
     * 同步商品信息
     *
     * @return RestResponse
     */
    @SysLog("同步直播商品信息")
    @RequestMapping("/getapproved")
    @RequiresPermissions("mall:roomallgoods:getapproved")
    public RestResponse getapproved(@RequestParam Map<String, String> params) {
        boolean flag = mallRoomAllGoodsService.getapproved(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("limit")));
        return flag ? RestResponse.success() : RestResponse.error("操作失败");
    }

    /**
     * 撤回审核
     *
     * @return RestResponse
     */
    @SysLog("撤回直播商品审核")
    @RequestMapping("/resetaudit")
    @RequiresPermissions("mall:roomallgoods:resetaudit")
    public RestResponse resetaudit(@RequestParam Map<String, String> params) {
        boolean flag = mallRoomAllGoodsService.resetaudit(Integer.parseInt(params.get("goodsId")), Integer.parseInt(params.get("auditId")));
        return flag ? RestResponse.success() : RestResponse.error("操作失败");
    }

    /**
     * 重新审核
     *
     * @return RestResponse
     */
    @SysLog("重新审核直播商品")
    @RequestMapping("/audit")
    @RequiresPermissions("mall:roomallgoods:audit")
    public RestResponse audit(@RequestParam Map<String, String> params) {
        boolean flag = mallRoomAllGoodsService.audit(Integer.parseInt(params.get("goodsId")));
        return flag ? RestResponse.success() : RestResponse.error("操作失败");
    }
}
