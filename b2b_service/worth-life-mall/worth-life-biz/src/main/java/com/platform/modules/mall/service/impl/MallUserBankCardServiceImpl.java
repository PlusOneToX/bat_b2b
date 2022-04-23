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
import com.platform.modules.mall.dao.MallUserBankCardDao;
import com.platform.modules.mall.entity.MallUserBankCardEntity;
import com.platform.modules.mall.service.MallUserBankCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户银行卡表Service实现类
 *
 * @author 李鹏军
 * @since 2020-06-15 09:42:45
 */
@Service("mallUserBankCardService")
public class MallUserBankCardServiceImpl extends ServiceImpl<MallUserBankCardDao, MallUserBankCardEntity> implements MallUserBankCardService {

    @Override
    public List<MallUserBankCardEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallUserBankCardEntity> page = new Query<MallUserBankCardEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallUserBankCardPage(page, params));
    }

    @Override
    public boolean add(MallUserBankCardEntity mallUserBankCard) {
        return this.save(mallUserBankCard);
    }

    @Override
    public boolean update(MallUserBankCardEntity mallUserBankCard) {
        return this.updateById(mallUserBankCard);
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
    public List<MallUserBankCardEntity> getCardList(QueryWrapper<MallUserBankCardEntity> qw) {
        return this.baseMapper.getCardList(qw);
    }

    @Override
    public MallUserBankCardEntity getCardSimpleInfoById(String cardId) {
        return this.baseMapper.getCardSimpleInfoById(cardId);
    }

    @Override
    public MallUserBankCardEntity getCardInfoById(Map<String, Object> params) {
        return this.baseMapper.getCardInfoById(params);
    }
}
