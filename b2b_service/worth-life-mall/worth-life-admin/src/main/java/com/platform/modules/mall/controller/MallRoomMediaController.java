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
import com.platform.modules.mall.entity.MallRoomMediaEntity;
import com.platform.modules.mall.service.MallRoomMediaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 直播房间回放视频Controller
 *
 * @author 李鹏军
 * @since 2020-03-30 18:52:08
 */
@RestController
@RequestMapping("mall/roommedia")
public class MallRoomMediaController extends AbstractController {
    @Autowired
    private MallRoomMediaService mallRoomMediaService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallRoomMediaEntity> list = mallRoomMediaService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询直播房间回放视频
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:roommedia:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallRoomMediaService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param roomid 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{roomid}")
    @RequiresPermissions("mall:roommedia:info")
    public RestResponse info(@PathVariable("roomid") Integer roomid) {
        MallRoomMediaEntity mallRoomMedia = mallRoomMediaService.getById(roomid);

        return RestResponse.success().put("roommedia", mallRoomMedia);
    }

    /**
     * 新增直播房间回放视频
     *
     * @param mallRoomMedia mallRoomMedia
     * @return RestResponse
     */
    @SysLog("新增直播房间回放视频")
    @RequestMapping("/save")
    @RequiresPermissions("mall:roommedia:save")
    public RestResponse save(@RequestBody MallRoomMediaEntity mallRoomMedia) {

        mallRoomMediaService.add(mallRoomMedia);

        return RestResponse.success();
    }

    /**
     * 修改直播房间回放视频
     *
     * @param mallRoomMedia mallRoomMedia
     * @return RestResponse
     */
    @SysLog("修改直播房间回放视频")
    @RequestMapping("/update")
    @RequiresPermissions("mall:roommedia:update")
    public RestResponse update(@RequestBody MallRoomMediaEntity mallRoomMedia) {

        mallRoomMediaService.update(mallRoomMedia);

        return RestResponse.success();
    }

    /**
     * 根据主键删除直播房间回放视频
     *
     * @param roomids roomids
     * @return RestResponse
     */
    @SysLog("删除直播房间回放视频")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:roommedia:delete")
    public RestResponse delete(@RequestBody Integer[] roomids) {
        mallRoomMediaService.deleteBatch(roomids);

        return RestResponse.success();
    }
}
