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
import com.platform.modules.mall.dao.MallDistInvitationDao;
import com.platform.modules.mall.entity.MallDistEntity;
import com.platform.modules.mall.entity.MallDistInvitationEntity;
import com.platform.modules.mall.service.MallDistInvitationService;
import com.platform.modules.mall.service.MallDistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 分销上级邀请绑定Service实现类
 *
 * @author 李鹏军
 * @since 2020-05-12 17:33:37
 */
@Service("mallDistInvitationService")
public class MallDistInvitationServiceImpl extends ServiceImpl<MallDistInvitationDao, MallDistInvitationEntity> implements MallDistInvitationService {
    @Override
    public List<MallDistInvitationEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallDistInvitationEntity> page = new Query<MallDistInvitationEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallDistInvitationPage(page, params));
    }

    @Override
    public boolean add(MallDistInvitationEntity mallDistInvitation) {
        return this.save(mallDistInvitation);
    }

    @Override
    public boolean update(MallDistInvitationEntity mallDistInvitation) {
        return this.updateById(mallDistInvitation);
    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }
}
