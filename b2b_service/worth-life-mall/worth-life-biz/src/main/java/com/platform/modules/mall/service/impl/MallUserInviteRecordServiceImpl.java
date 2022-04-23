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
import com.platform.modules.mall.dao.MallUserInviteRecordDao;
import com.platform.modules.mall.entity.MallUserInviteRecordEntity;
import com.platform.modules.mall.service.MallUserInviteRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 邀请记录表Service实现类
 *
 * @author pfrong
 * @since 2020-11-10 14:35:24
 */
@Service("mallUserInviteRecordService")
public class MallUserInviteRecordServiceImpl extends ServiceImpl<MallUserInviteRecordDao, MallUserInviteRecordEntity> implements MallUserInviteRecordService {

    @Override
    public List<MallUserInviteRecordEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallUserInviteRecordPage(page, params));
    }

    @Override
    public boolean add(MallUserInviteRecordEntity mallUserInviteRecord) {
        return this.save(mallUserInviteRecord);
    }

    @Override
    public boolean update(MallUserInviteRecordEntity mallUserInviteRecord) {
        return this.updateById(mallUserInviteRecord);
    }

    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public Page queryPageById(Map<String, Object> params) {
        //排序
        params.put("sidx", "id");
        params.put("asc", false);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        return page.setRecords(baseMapper.queryPageById(page, params));
    }

    @Override
    public Page<MallUserInviteRecordEntity> inviteShopList(Map<String, Object> params) {
        params.put("sidx", "invite_user_id");
        params.put("desc", true);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        return baseMapper.inviteShopList(page, params);
    }

    @Override
    public Page<MallUserInviteRecordEntity> extendUserRecord(Map<String, Object> params) {
        params.put("sidx", "user_id");
        params.put("desc", true);
        Page<MallUserInviteRecordEntity> page = new Query<MallUserInviteRecordEntity>(params).getPage();
        return baseMapper.extendUserRecord(page, params);
    }

    //查询店铺的直推间推用户
    public List<MallUserInviteRecordEntity> queryUserAll(String id) {
      return baseMapper.queryUserAll(id);
    }


}
