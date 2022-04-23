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
import com.platform.modules.mall.dao.MallDistributionConfDao;
import com.platform.modules.mall.entity.MallDistributionConfEntity;
import com.platform.modules.mall.service.MallDistributionConfService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 李鹏军
 * @since 2021-01-07 10:37:39
 */
@Service("mallDistributionConfService")
public class MallDistributionConfServiceImpl extends ServiceImpl<MallDistributionConfDao, MallDistributionConfEntity> implements MallDistributionConfService {

    @Override
    public List<MallDistributionConfEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallDistributionConfEntity> page = new Query<MallDistributionConfEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallDistributionConfPage(page, params));
    }

    @Override
    public boolean add(MallDistributionConfEntity mallDistributionConf) {
        return this.save(mallDistributionConf);
    }

    @Override
    public boolean update(MallDistributionConfEntity mallDistributionConf) {
        return this.updateById(mallDistributionConf);
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


    public Page queryList(Map<String, Object> params) {
        params.put("sidx", "level");
        params.put("asc", false);
        Page<MallDistributionConfEntity> page = new Query<MallDistributionConfEntity>(params).getPage();
        return page.setRecords(baseMapper.queryList(page, params));
    }

}
