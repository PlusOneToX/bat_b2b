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
import com.platform.modules.mall.entity.MallRoomEntity;
import com.platform.modules.mall.service.MallRoomService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 直播房间信息Controller
 *
 * @author 李鹏军
 * @since 2020-03-30 18:52:09
 */
@RestController
@RequestMapping("mall/room")
public class MallRoomController extends AbstractController {
    @Autowired
    private MallRoomService mallRoomService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:room:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallRoomEntity> list = mallRoomService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询直播房间信息
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:room:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallRoomService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param roomid 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{roomid}")
    @RequiresPermissions("mall:room:info")
    public RestResponse info(@PathVariable("roomid") Integer roomid) {
        MallRoomEntity mallRoom = mallRoomService.getById(roomid);

        return RestResponse.success().put("room", mallRoom);
    }

    /**
     * 新增直播房间信息
     *
     * @param mallRoom mallRoom
     * @return RestResponse
     */
    @SysLog("新增直播房间信息")
    @RequestMapping("/save")
    @RequiresPermissions("mall:room:save")
    public RestResponse save(@RequestBody MallRoomEntity mallRoom) {

        mallRoomService.add(mallRoom);

        return RestResponse.success();
    }

    /**
     * 修改直播房间信息
     *
     * @param mallRoom mallRoom
     * @return RestResponse
     */
    @SysLog("修改直播房间信息")
    @RequestMapping("/update")
    @RequiresPermissions("mall:room:update")
    public RestResponse update(@RequestBody MallRoomEntity mallRoom) {

        mallRoomService.update(mallRoom);

        return RestResponse.success();
    }

    /**
     * 根据主键删除直播房间信息
     *
     * @param roomids roomids
     * @return RestResponse
     */
    @SysLog("删除直播房间信息")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:room:delete")
    public RestResponse delete(@RequestBody Integer[] roomids) {
        mallRoomService.deleteBatch(roomids);

        return RestResponse.success();
    }

    /**
     * 直播间导入商品
     *
     * @param params params
     * @return RestResponse
     */
    @SysLog("直播间导入商品")
    @RequestMapping("/addgoods")
    @RequiresPermissions("mall:room:addgoods")
    public RestResponse addgoods(@RequestBody Map<String, Object> params) {

        boolean flag = mallRoomService.addgoods(params);
        return flag ? RestResponse.success() : RestResponse.error("操作失败");
    }

    /**
     * 同步直播间信息
     *
     * @return RestResponse
     */
    @SysLog("同步直播间信息")
    @RequestMapping("/getLiveInfo")
    @RequiresPermissions("mall:room:getLiveInfo")
    public RestResponse getLiveInfo(@RequestParam Map<String, String> params) {
        boolean flag = mallRoomService.getLiveInfo(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("limit")));
        return flag ? RestResponse.success() : RestResponse.error("操作失败");
    }
}
