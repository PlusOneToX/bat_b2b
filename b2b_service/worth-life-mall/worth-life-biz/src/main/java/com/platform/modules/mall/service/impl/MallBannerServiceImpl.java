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
import com.platform.modules.mall.dao.MallBannerDao;
import com.platform.modules.mall.entity.MallBannerEntity;
import com.platform.modules.mall.service.MallBannerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 首页轮播配置Service实现类
 *
 * @author 李鹏军
 * @since 2019-07-01 10:57:03
 */
@Service("mallBannerService")
public class MallBannerServiceImpl extends ServiceImpl<MallBannerDao, MallBannerEntity> implements MallBannerService {

    @Override
    public List<MallBannerEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ID");
        params.put("asc", false);
        Page<MallBannerEntity> page = new Query<MallBannerEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallBannerPage(page, params));
    }

    @Override
    public boolean add(MallBannerEntity mallBanner) {
        return this.save(mallBanner);
    }

    @Override
    public boolean update(MallBannerEntity mallBanner) {
        return this.updateById(mallBanner);
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
}
