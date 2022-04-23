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
package com.platform.modules.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.mall.entity.MallRoomMediaEntity;

import java.util.List;
import java.util.Map;

/**
 * 直播房间回放视频Service接口
 *
 * @author 李鹏军
 * @since 2020-03-30 18:52:08
 */
public interface MallRoomMediaService extends IService<MallRoomMediaEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallRoomMediaEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询直播房间回放视频
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增直播房间回放视频
     *
     * @param mallRoomMedia 直播房间回放视频
     * @return 新增结果
     */
    boolean add(MallRoomMediaEntity mallRoomMedia);

    /**
     * 根据主键更新直播房间回放视频
     *
     * @param mallRoomMedia 直播房间回放视频
     * @return 更新结果
     */
    boolean update(MallRoomMediaEntity mallRoomMedia);

    /**
     * 根据主键删除直播房间回放视频
     *
     * @param roomid roomid
     * @return 删除结果
     */
    boolean delete(Integer roomid);

    /**
     * 根据主键批量删除
     *
     * @param roomids roomids
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] roomids);
}
