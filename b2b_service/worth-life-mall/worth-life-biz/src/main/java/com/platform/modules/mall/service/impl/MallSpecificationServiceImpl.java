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
import com.platform.modules.mall.dao.MallSpecificationDao;
import com.platform.modules.mall.entity.MallSpecificationEntity;
import com.platform.modules.mall.service.MallSpecificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SKU键Service实现类
 *
 * @author 李鹏军
 * @since 2019-07-03 23:15:44
 */
@Service("mallSpecificationService")
public class MallSpecificationServiceImpl extends ServiceImpl<MallSpecificationDao, MallSpecificationEntity> implements MallSpecificationService {

    @Override
    public List<MallSpecificationEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ID");
        params.put("asc", false);
        Page<MallSpecificationEntity> page = new Query<MallSpecificationEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallSpecificationPage(page, params));
    }

    @Override
    public boolean add(MallSpecificationEntity mallSpecification) {
        return this.save(mallSpecification);
    }

    @Override
    public boolean update(MallSpecificationEntity mallSpecification) {
        return this.updateById(mallSpecification);
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
