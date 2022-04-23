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
package com.platform.modules.mall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallRoomMediaDao;
import com.platform.modules.mall.entity.MallRoomMediaEntity;
import com.platform.modules.mall.service.MallRoomMediaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 直播房间回放视频Service实现类
 *
 * @author 李鹏军
 * @since 2020-03-30 18:52:08
 */
@Service("mallRoomMediaService")
public class MallRoomMediaServiceImpl extends ServiceImpl<MallRoomMediaDao, MallRoomMediaEntity> implements MallRoomMediaService {

    @Override
    public List<MallRoomMediaEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.roomid");
        params.put("asc", false);
        Page<MallRoomMediaEntity> page = new Query<MallRoomMediaEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallRoomMediaPage(page, params));
    }

    @Override
    public boolean add(MallRoomMediaEntity mallRoomMedia) {
        return this.save(mallRoomMedia);
    }

    @Override
    public boolean update(MallRoomMediaEntity mallRoomMedia) {
        return this.updateById(mallRoomMedia);
    }

    @Override
    public boolean delete(Integer roomid) {
        return this.removeById(roomid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] roomids) {
        return this.removeByIds(Arrays.asList(roomids));
    }
}
