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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.dao.MallDistPromoDao;
import com.platform.modules.mall.entity.MallDistEntity;
import com.platform.modules.mall.entity.MallDistPromoEntity;
import com.platform.modules.mall.service.MallDistPromoService;
import com.platform.modules.mall.service.MallDistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 推广追踪表Service实现类
 *
 * @author 林伟
 * @since 2020-06-09 11:59:32
 */
@Service("mallDistPromoService")
public class MallDistPromoServiceImpl extends ServiceImpl<MallDistPromoDao, MallDistPromoEntity> implements MallDistPromoService {

    @Autowired
    private MallDistService mallDistService;

    @Override
    public List<MallDistPromoEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallDistPromoEntity> page = new Query<MallDistPromoEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallDistPromoPage(page, params));
    }

    @Override
    public boolean add(MallDistPromoEntity mallDistPromo) {
        return this.save(mallDistPromo);
    }

    @Override
    public boolean update(MallDistPromoEntity mallDistPromo) {
        return this.updateById(mallDistPromo);
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
    public void addPromoRecord(String userId, MallDistPromoEntity mallDistPromoEntity) {
        String referrerUserId = mallDistPromoEntity.getUserId();
        // 存在且非本人
        if (StringUtils.trimToNull(referrerUserId) == null || userId.equals(referrerUserId))
            return ;

        MallDistEntity promoDistEntity = mallDistService.getOne(new QueryWrapper<MallDistEntity>().eq("USER_ID", mallDistPromoEntity.getUserId()));
        if (promoDistEntity == null)
            return ;// 推广者不是分销商

        MallDistEntity selfDistEntity = mallDistService.getOne(new QueryWrapper<MallDistEntity>().eq("USER_ID", userId));
        // 不可分享给直接上级分销商：自己是分销商且推广者是自己的直接下级
        if (selfDistEntity != null && selfDistEntity.getId().equals(promoDistEntity.getSuperiorId()))
            return;

        String id = mallDistPromoEntity.getId();
        if (id == null) {
            // 添加
            this.add(mallDistPromoEntity);
        } else {
            // 更新
            this.update(mallDistPromoEntity);
        }

    }

    @Override
    public List<MallDistPromoEntity> getPromoListByCartIds(List<String> mallCartIds) {
        return baseMapper.selectList(new QueryWrapper<MallDistPromoEntity>().in("CART_ID", mallCartIds));
    }

    @Override
    public List<MallDistPromoEntity> getPromoListByOrderId(String orderId) {
        return baseMapper.selectList(new QueryWrapper<MallDistPromoEntity>().eq("ORDER_ID", orderId));
    }

}
